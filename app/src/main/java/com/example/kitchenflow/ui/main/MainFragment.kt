package com.example.kitchenflow.ui.main

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kitchenflow.R
import com.example.kitchenflow.data.entity.SortType
import com.example.kitchenflow.data.entity.getSortTypeString
import com.example.kitchenflow.databinding.MainFragmentBinding
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by inject()
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        viewModel.orders.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                adapter.addAll(it)
            }
        }
    }

    private fun initView() {
        with(binding) {
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_variations,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        when (parent?.getItemAtPosition(position) as String) {
                            resources.getSortTypeString(SortType.PICKUP) -> {
                                this@MainFragment.adapter.sort(SortType.PICKUP)
                            }
                            resources.getSortTypeString(SortType.PREPARATION) -> {
                                this@MainFragment.adapter.sort(SortType.PREPARATION)
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
            adapter = OrdersAdapter(requireContext())
            ordersListRv.adapter = adapter
        }
    }
}