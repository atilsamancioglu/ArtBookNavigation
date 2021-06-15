package com.atilsamancioglu.artbooknavigation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.atilsamancioglu.artbooknavigation.databinding.FragmentArtListBinding


class ArtList : Fragment() {

    val artNameList = ArrayList<String>()
    val artIdList = ArrayList<Int>()
    private lateinit var artAdapter : ArtListAdapter
    private var _binding: FragmentArtListBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


        artAdapter = ArtListAdapter(artNameList,artIdList)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = artAdapter

        getFromSQL()

    }

    fun getFromSQL() {
        try {
            if (activity != null) {

                val database = requireActivity().openOrCreateDatabase("Arts", Context.MODE_PRIVATE,null)

                val cursor = database.rawQuery("SELECT * FROM arts",null)
                val artNameIx = cursor.getColumnIndex("artname")
                val idIx = cursor.getColumnIndex("id")

                artIdList.clear()
                artNameList.clear()

                while (cursor.moveToNext()) {
                    artNameList.add(cursor.getString(artNameIx))
                    artIdList.add(cursor.getInt(idIx))
                }

                artAdapter.notifyDataSetChanged()

                cursor.close()
            }




        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
