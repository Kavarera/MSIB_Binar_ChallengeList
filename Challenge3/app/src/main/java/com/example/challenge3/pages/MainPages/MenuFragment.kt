package com.example.challenge3.pages.MainPages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challenge3.R
import com.example.challenge3.util.adapter.MainMenuRVAdapter
import com.example.challenge3.databinding.FragmentMenuBinding
import com.example.challenge3.models.CategoryFoodData
import com.example.challenge3.util.preferences.PreferencesHelper
import com.example.challenge3.models.Food
import com.example.challenge3.models.enumclass.EnumRecyclerViewOption
import com.example.challenge3.models.enumclass.EnumStatus
import com.example.challenge3.util.viewmodels.MenuViewModel
import com.google.gson.Gson
import org.koin.android.ext.android.inject

class MenuFragment : Fragment() {
    //viewmodel

    private lateinit var binding:FragmentMenuBinding
    private val viewModel:MenuViewModel by inject()
    private lateinit var foodListAPI: List<Food>
    private var foodList= listOf<Food>()
    private var gridOption:Int = 0
    private var testingAPIFoodCategories:List<CategoryFoodData>? = null

    private lateinit var rv:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater,container,false)
        rv=binding.rvMenuMakanan

        fetchCategoriesData()
        fetchFoods()




        gridOption = PreferencesHelper.layoutOption
        rv.layoutManager = if(gridOption== EnumRecyclerViewOption.LINEAR_LAYOUT.value){
            LinearLayoutManager(requireContext())
        } else{
            GridLayoutManager(requireContext(),2)
        }
        Log.d("gridOption","${rv.layoutManager.toString()} --- ${rv.layoutManager}")
        var adapter = if(gridOption== EnumRecyclerViewOption.LINEAR_LAYOUT.value){
            MainMenuRVAdapter(requireContext(), foodList, EnumRecyclerViewOption.LINEAR_LAYOUT)
        } else{
            MainMenuRVAdapter(requireContext(),foodList, EnumRecyclerViewOption.GRID_LAYOUT)

        }

//            MainMenuRVAdapter(foodList,RecyclerViewOption.LINEAR_LAYOUT)
        adapter.setOnItemClickCallback(object : MainMenuRVAdapter.IonItemClickCallback{
            override fun onItemClicked(data: Food) {
                val mb = Bundle().apply {
                    putParcelable("data",data)
                }
                findNavController().navigate(R.id.action_menuFragment_to_foodDetailActivity,mb)
            }

        })
        rv.adapter=adapter

        binding.ibGridOption.setOnClickListener {
            if(gridOption== EnumRecyclerViewOption.LINEAR_LAYOUT.value){
                gridOption= EnumRecyclerViewOption.GRID_LAYOUT.value
                //mySharedPreferences.layoutOption=gridOption
                PreferencesHelper.layoutOption=gridOption
                binding.ibGridOption.setImageResource(R.drawable.ic_menu)
                rv.layoutManager = GridLayoutManager(requireContext(),2)
                adapter = MainMenuRVAdapter(requireContext(),foodList, EnumRecyclerViewOption.GRID_LAYOUT)
                adapter.setOnItemClickCallback(object : MainMenuRVAdapter.IonItemClickCallback{
                    override fun onItemClicked(data: Food) {
                        val mb = Bundle().apply {
                            putParcelable("data",data)
                        }
                        it.findNavController().navigate(R.id.action_menuFragment_to_foodDetailActivity,mb)
                    }

                })
                rv.adapter=adapter
            }
            else{
                gridOption= EnumRecyclerViewOption.LINEAR_LAYOUT.value
                PreferencesHelper.layoutOption = gridOption
                binding.ibGridOption.setImageResource(R.drawable.ic_list)
                rv.layoutManager = LinearLayoutManager(requireContext())
                adapter = MainMenuRVAdapter(requireContext(),foodList, EnumRecyclerViewOption.LINEAR_LAYOUT)
                adapter.setOnItemClickCallback(object : MainMenuRVAdapter.IonItemClickCallback{
                    override fun onItemClicked(data: Food) {
                        val mb = Bundle().apply {
                            putParcelable("data",data)
                        }
                        it.findNavController().navigate(R.id.action_menuFragment_to_foodDetailActivity,mb)
                    }

                })
                rv.adapter=adapter
            }
        }



        return binding.root
    }


    fun fetchCategoriesData(){//Using coroutines
        viewModel.fetchCategories().observe(viewLifecycleOwner){data->
            when(data.status){
                EnumStatus.ERROR->{
                    Log.e("API","Error API Menu Fragment")
                }
                EnumStatus.SUCCESS->{
                    Log.d("testAPI","get data berhasil = ${Gson().toJson(data.data)}")
                    testingAPIFoodCategories = data.data?.data
                    Glide.with(requireContext())
                        .load(testingAPIFoodCategories?.get(0)?.imageUrl)
                        .into(binding.incl1.incl11.ivItem)
                    Glide.with(requireContext())
                        .load(testingAPIFoodCategories?.get(1)?.imageUrl)
                        .centerInside()
                        .into(binding.incl1.incl12.ivItem)
                    Glide.with(requireContext())
                        .load(testingAPIFoodCategories?.get(2)?.imageUrl)
                        .centerCrop()
                        .into(binding.incl1.incl13.ivItem)
                    Glide.with(requireContext())
                        .load(testingAPIFoodCategories?.get(3)?.imageUrl)
                        .fitCenter()
                        .into(binding.incl1.incl14.ivItem)

                    binding.incl1.incl11.tvNamaFood.text=testingAPIFoodCategories?.get(0)?.nama

                    binding.incl1.incl11.ivItem.setOnClickListener {
                        categoryClicked(binding.incl1.incl11.tvNamaFood.text.toString().lowercase())
                    }

                    binding.incl1.incl12.tvNamaFood.text=testingAPIFoodCategories?.get(1)?.nama

                    binding.incl1.incl12.ivItem.setOnClickListener {
                        categoryClicked(binding.incl1.incl12.tvNamaFood.text.toString().lowercase())
                    }

                    binding.incl1.incl13.tvNamaFood.text=testingAPIFoodCategories?.get(2)?.nama

                    binding.incl1.incl13.ivItem.setOnClickListener {
                        categoryClicked(binding.incl1.incl13.tvNamaFood.text.toString().lowercase())
                    }

                    binding.incl1.incl14.tvNamaFood.text=testingAPIFoodCategories?.get(3)?.nama

                    binding.incl1.incl14.ivItem.setOnClickListener {
                        categoryClicked(binding.incl1.incl14.tvNamaFood.text.toString().lowercase())
                    }
                }
                EnumStatus.LOADING->{

                }
            }
        }

    }

    fun categoryClicked(category:String){
        viewModel.fetchAllFoods(category).observe(viewLifecycleOwner){
            when(it.status){
                EnumStatus.SUCCESS->{
                    foodListAPI=it.data?.data!!
                    foodList=it.data.data

                    var adapter = if(gridOption== EnumRecyclerViewOption.LINEAR_LAYOUT.value){
                        MainMenuRVAdapter(requireContext(), foodList, EnumRecyclerViewOption.LINEAR_LAYOUT)
                    } else{
                        MainMenuRVAdapter(requireContext(),foodList, EnumRecyclerViewOption.GRID_LAYOUT)

                    }
                    adapter.setOnItemClickCallback(object : MainMenuRVAdapter.IonItemClickCallback{
                        override fun onItemClicked(data: Food) {
                            val mb = Bundle().apply {
                                putParcelable("data",data)
                            }
                            findNavController().navigate(R.id.action_menuFragment_to_foodDetailActivity,mb)
                        }

                    })
                    rv.adapter=adapter

                }
                EnumStatus.ERROR->{
                    Log.e("API","Error API Menu Fragment")
                }
                else->{

                }
            }
        }
    }

    fun fetchFoods(){
        viewModel.fetchAllFoods().observe(viewLifecycleOwner){
            data->
            when(data.status){
                EnumStatus.SUCCESS->{
                    foodListAPI = data.data?.data!!
                    foodList=data.data.data
                    binding.ibGridOption.performClick()
                    binding.ibGridOption.performClick()

                }
                EnumStatus.ERROR->{
                    Log.e("API","Error API Menu Fragment")
                }
                EnumStatus.LOADING->{

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}