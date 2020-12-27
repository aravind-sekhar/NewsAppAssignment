package com.example.newsappassignment.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsappassignment.MyApplication
import com.example.newsappassignment.R
import com.example.newsappassignment.adapters.FactAdapter
import com.example.newsappassignment.databinding.FactsFragmentBinding
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.utils.NetworkUtility
import com.example.newsappassignment.viewmodels.MainViewModel

class FactsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        fun newInstance() = FactsFragment()
    }

    private lateinit var factAdapter: FactAdapter
    private lateinit var factsListView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingText: TextView
    private lateinit var rlProgressView: ConstraintLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var factsList: List<Fact>

    private val viewModel by viewModels<MainViewModel>() {
        MainViewModel.FactsViewModelFactory((requireContext().applicationContext as MyApplication).factsDataRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factsFragmentBinding = FactsFragmentBinding.inflate(inflater, container, false)
        factsListView = factsFragmentBinding.rvFactsList
        factsListView.layoutManager = LinearLayoutManager(context)
        factsListView.setHasFixedSize(true)
        swipeRefreshLayout = factsFragmentBinding.swipeRefreshView
        swipeRefreshLayout.setOnRefreshListener(this)
        progressBar = factsFragmentBinding.progressBar
        rlProgressView = factsFragmentBinding.rlProgressView
        loadingText = factsFragmentBinding.tvLoadingText
        return factsFragmentBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFactCategory().observe(viewLifecycleOwner, Observer {
            swipeRefreshLayout.isRefreshing = false
            it ?. let {
                requireActivity().title = it.title
            }

        })
        viewModel.getFacts().observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                loadingText.text = requireContext().getString(R.string.no_data)
                progressBar.visibility = View.INVISIBLE
                rlProgressView.visibility = View.VISIBLE
                factsListView.visibility = View.GONE
            } else {
                rlProgressView.visibility = View.GONE
                factsListView.visibility = View.VISIBLE
                factsList = it
                factAdapter = FactAdapter(factsList)
                factsListView.adapter = factAdapter
            }
        })
        viewModel.getStatusMsg().observe(viewLifecycleOwner, Observer {
            if (it.isNotBlank()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false
            }
            viewModel.setStatusMsg("")

        })

        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        when {
            // Register network callback to monitor network changes.
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {

                val networkRequest = NetworkRequest.Builder().build()
                connectivityManager.registerNetworkCallback(networkRequest, object :
                    ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        syncDB()
                    }
                })
            }
            // To support Android versions below lollipop
            NetworkUtility.internetCheck(requireContext()) -> {
                syncDB()
            }
            else -> {
                viewModel.setStatusMsg(getString(R.string.networkConnectivityError))
            }
        }
    }


    fun syncDB() {
        viewModel.isDataSynced().value?. let {
            if(!it){
                Toast.makeText(requireContext(), getString(R.string.syncingData), Toast.LENGTH_LONG)
                    .show()
                viewModel.syncDataFromAPI()
            }
        }
    }


    override fun onRefresh() {
        Toast.makeText(
            context,
            requireContext().getString(R.string.syncingData),
            Toast.LENGTH_SHORT
        ).show()
        viewModel.syncDataFromAPI(true)
    }


}