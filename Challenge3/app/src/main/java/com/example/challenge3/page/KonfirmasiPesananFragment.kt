package com.example.challenge3.page

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challenge3.R
import com.example.challenge3.ViewModelFactory
import com.example.challenge3.adapters.KeranjangRecyclerViewAdapter
import com.example.challenge3.databinding.FragmentKonfirmasiPesananBinding
import com.example.challenge3.dialog.DialogPesananBerhasil
import com.example.challenge3.models.EnumMetodePembayaran
import com.example.challenge3.models.EnumMetodePengiriman
import com.example.challenge3.models.FoodKeranjang
import com.example.challenge3.viewmodels.FoodViewModel
import com.example.challenge3.viewmodels.KonfirmasiPesananViewModel
import com.example.challenge3.viewmodels.MainActivityViewModel

class KonfirmasiPesananFragment : Fragment() {

    private lateinit var foodViewModel: FoodViewModel
    private lateinit var keranjangViewModel: KonfirmasiPesananViewModel
    private lateinit var mainViewModel:MainActivityViewModel
    private lateinit var binding:FragmentKonfirmasiPesananBinding
    private lateinit var listPesanan:LiveData<List<FoodKeranjang>>
    private lateinit var adapter:KeranjangRecyclerViewAdapter
    private lateinit var pembayaran: LiveData<EnumMetodePembayaran>
    private lateinit var pengiriman: LiveData<EnumMetodePengiriman>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodViewModel = ViewModelProvider(requireActivity(),
            ViewModelFactory(requireActivity().application))
            .get(FoodViewModel::class.java)

        keranjangViewModel=ViewModelProvider(requireActivity(),
            ViewModelFactory(requireActivity().application))
            .get(KonfirmasiPesananViewModel::class.java)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        mainViewModel.setVisibleBottomNav(false)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKonfirmasiPesananBinding.inflate(inflater,container,false)
        val recyclerView = binding.rvListKeranjang
        listPesanan = foodViewModel.getAllFoods()
        listPesanan.observe(viewLifecycleOwner){
            item->
            if(item.size==0){
                findNavController().popBackStack();
            }
            var t =0
            item.forEach {
                t = t+(it.quantity *it.foodPrice)
            }
            binding.tvHargaTotalPesanan.text="Rp. ${t.toString()}"
            adapter= KeranjangRecyclerViewAdapter(item,foodViewModel)
            recyclerView.adapter=adapter
            recyclerView.layoutManager=LinearLayoutManager(requireContext())
        }
        pembayaran = keranjangViewModel.tipePembayaran()
        pengiriman = keranjangViewModel.tipePengiriman()

        pembayaran.observe(viewLifecycleOwner){
            Log.d("konfirmasipesanan","nilai pembayaran = ${it}")
            if(it==EnumMetodePembayaran.TUNAI){
                binding.btnTunai.backgroundTintList= ColorStateList
                    .valueOf(ContextCompat
                        .getColor(requireContext()
                            ,R.color.grey))
                binding.btnDompetDigital.backgroundTintList= ColorStateList
                    .valueOf(ContextCompat
                        .getColor(requireContext()
                            ,R.color.purple_200))
            }
            else{
                binding.btnTunai.backgroundTintList= ColorStateList
                    .valueOf(ContextCompat
                        .getColor(requireContext()
                            ,R.color.purple_200))
                binding.btnDompetDigital.backgroundTintList= ColorStateList
                    .valueOf(ContextCompat
                        .getColor(requireContext()
                            ,R.color.grey))
            }
        }
        pengiriman.observe(viewLifecycleOwner){
            if(it==EnumMetodePengiriman.DIKIRIM){
                binding.btnDikirim.backgroundTintList= ColorStateList
                    .valueOf(ContextCompat
                        .getColor(requireContext()
                            ,R.color.grey))
                binding.btnAmbilSendiri.backgroundTintList= ColorStateList
                    .valueOf(ContextCompat
                        .getColor(requireContext()
                            ,R.color.purple_200))
            }
            else{
                binding.btnDikirim.backgroundTintList= ColorStateList
                    .valueOf(ContextCompat
                        .getColor(requireContext()
                            ,R.color.purple_200))
                binding.btnAmbilSendiri.backgroundTintList= ColorStateList
                    .valueOf(ContextCompat
                        .getColor(requireContext()
                            ,R.color.grey))
            }
        }
        binding.btnTunai.setOnClickListener {
            keranjangViewModel.switchMetodePembayaran(EnumMetodePembayaran.TUNAI)
            Log.d("konfirmasipesanan","Switched metode pembayaran to ${pembayaran.value}")
        }
        binding.btnDompetDigital.setOnClickListener {
            keranjangViewModel.switchMetodePembayaran(EnumMetodePembayaran.DOMPET_DIGITAL)
            Log.d("konfirmasipesanan","Switched metode pembayaran to ${pembayaran.value}")
        }

        binding.btnDikirim.setOnClickListener {
            keranjangViewModel.switchMetodePengiriman(EnumMetodePengiriman.DIKIRIM)
        }
        binding.btnAmbilSendiri.setOnClickListener {
            keranjangViewModel.switchMetodePengiriman(EnumMetodePengiriman.AMBIL_SENDIRI)
        }
        binding.btnCheckout.setOnClickListener {
            DialogPesananBerhasil().show(requireActivity().supportFragmentManager,"dialogPesananBerhasil")
        }




       return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainViewModel.setVisibleBottomNav(true)
    }

}