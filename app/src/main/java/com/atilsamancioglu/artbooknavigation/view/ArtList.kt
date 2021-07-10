package com.atilsamancioglu.artbooknavigation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.atilsamancioglu.artbooknavigation.adapter.ArtListAdapter
import com.atilsamancioglu.artbooknavigation.databinding.FragmentArtListBinding
import com.atilsamancioglu.artbooknavigation.model.Art
import com.atilsamancioglu.artbooknavigation.roomdb.ArtDao
import com.atilsamancioglu.artbooknavigation.roomdb.ArtDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ArtList : Fragment() {

    private lateinit var artAdapter : ArtListAdapter
    private var _binding: FragmentArtListBinding? = null
    private val binding get() = _binding!!
    private val mDisposable = CompositeDisposable()
    private lateinit var artDao : ArtDao
    private lateinit var artDatabase : ArtDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        artDatabase = Room.databaseBuilder(requireContext(), ArtDatabase::class.java, "Arts").build()
        artDao = artDatabase.artDao()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArtListBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getFromSQL()

    }

    fun getFromSQL() {
        mDisposable.add(artDao.getArtWithNameAndId()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::handleResponse))
    }

    private fun handleResponse(artList: List<Art>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        artAdapter = ArtListAdapter(artList)
        binding.recyclerView.adapter = artAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
