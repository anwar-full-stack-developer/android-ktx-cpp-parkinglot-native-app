package com.example.parkinglot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parkinglot.data.ApiInterface
import com.example.parkinglot.data.DataParkinglot
import com.example.parkinglot.data.ResponseListParkinglot
import com.example.parkinglot.data.RetrofitClient
import com.example.parkinglot.placeholder.PlaceholderContent
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ParkinglotListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParkinglotListFragment : Fragment() {

    private var columnCount = 1
    private var editBack = 0
    private var editPosition = 0

    lateinit var rvAdapter: ParkinglotRecyclerViewAdapter
    lateinit var rv: RecyclerView
    lateinit var rvData: PlaceholderContent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvData = PlaceholderContent

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            editBack = it.getInt(ARG_EDIT_BACK)
            editPosition = it.getInt(ARG_EDIT_POSITION)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_parkinglot_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            rv = view
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ParkinglotRecyclerViewAdapter(PlaceholderContent.ITEMS)
            }
            rvAdapter = rv.adapter as ParkinglotRecyclerViewAdapter

            // Applying OnClickListener to our Adapter
            rvAdapter.setOnClickListener(object : ParkinglotRecyclerViewAdapter.OnClickListener {
                override fun onClick(position: Int, model: DataParkinglot) {
                    val d: DataParkinglot? = rvData.getItemData(position)
                    Log.d("Parkinglot", "Item on clicked")
                }

                override fun onLongClick(
                    position: Int,
                    model: DataParkinglot
                ) {
                    Log.d("Parkinglot", "Item on Long Clicked / Press")
                }

                override fun onClickToEdit(
                    position: Int,
                    model: DataParkinglot
                ) {
                    val d: DataParkinglot? = rvData.getItemData(position)
                    val actionType = "EDIT" // EDIT | NEW

                    Log.d("Parkinglot", "Parkinglot list EDIT btn clicked on clicked")
                    Log.d("Parkinglot", d.toString())
                    val fr = parentFragmentManager.beginTransaction()
                    fr.replace(R.id.nav_host_fragment_content_main, ParkinglotEditFragment.newInstance(
                        actionType,
                        position
                    ))
                    fr.commit()
                }

                override fun onClickToDelete(
                    position: Int,
                    model: DataParkinglot
                ) {
                    val d: DataParkinglot? = rvData.getItemData(position)
                    rvData.removeItem(position)
                    rvAdapter.submitList(PlaceholderContent.ITEMS)
//                        rvAdapter.notifyDataSetChanged()
//                        TODO: server-side api implementation
                    if (d != null) {
//                        deleteParkinglot(d)
                        val id = PlaceholderContent.itemToId(d)
                        deleteParkinglotFromJNI(id)
                    }
                }
            })
            rvAdapter.notifyDataSetChanged()

        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getParkinglotList()
        getParkinglotListNative()
//        rvAdapter.notifyDataSetChanged()
        Log.i("Parkinglot", "Adapter Created")
        Log.i("Parkinglot", "Adapter Created, view Created")
        Log.i("Parkinglot", "Adapter Data Size: " + rvAdapter.getItemCount().toString())
    }

    private fun notifyDataChanged(){

        rvAdapter.submitList(PlaceholderContent.ITEMS)
//        Log.d("Parkinglot: ", "Adapter List Count: " + rvAdapter.getItemCount().toString())
        rvAdapter.notifyDataSetChanged()
//        Log.d("Parkinglot: ", "Adapter List Count: = " + rvAdapter.getItemCount().toString())
    }

    // convert c/c++ std::string str to java string UTF-8
    private fun convert_jbyteArray_to_jstring(responseByteArray: ByteArray): String {
        return String(responseByteArray, charset("UTF-8"))
    }

    private fun getParkinglotListNative() {
//        val responseByteArray = getParkinglotListFromJNI()
//        // convert byte array to string
//        val response = String(responseByteArray, charset("UTF-8"))
        val response = convert_jbyteArray_to_jstring(getParkinglotListFromJNI())
        var gson = Gson()
        var rd = gson?.fromJson(response, ResponseListParkinglot::class.java)
        val dList: List<DataParkinglot>? = rd?.data
        if (dList != null) {
            PlaceholderContent.clearAll()
//                        Log.d("Parkinglot: ", "Adapter List Count: " + rvAdapter.getItemCount().toString())
            for (i in 0.. dList.size - 1) {
//                            Log.d("Parkinglot: ", "AdapterItem: " + dList[i].toString())
                PlaceholderContent.addItem(dList[i])
            }
//                        if (! rv.isComputingLayout())
//                        rvAdapter.submitList(PlaceholderContent.ITEMS)
            notifyDataChanged()
            Log.d("Parkinglot: ", "Adapter List Count:= " + rvAdapter.getItemCount().toString())
//                        rvAdapter.notifyDataSetChanged()
        }
    }
    external fun getParkinglotListFromJNI(): ByteArray

    private fun getParkinglotList() {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val response = apiInterface.getAllParkinglot()
                if (response.isSuccessful()) {
                    //your code for handling success response
//                    Log.d("Parkinglot: ", response.body().toString())

                    val dList: List<DataParkinglot>? = response.body()?.data
                    if (dList != null) {
                        PlaceholderContent.clearAll()
//                        Log.d("Parkinglot: ", "Adapter List Count: " + rvAdapter.getItemCount().toString())
                        for (i in 0.. dList.size - 1) {
//                            Log.d("Parkinglot: ", "AdapterItem: " + dList[i].toString())
                            PlaceholderContent.addItem(dList[i])
                        }
//                        if (! rv.isComputingLayout())
//                        rvAdapter.submitList(PlaceholderContent.ITEMS)
                        notifyDataChanged()
                        Log.d("Parkinglot: ", "Adapter List Count:= " + rvAdapter.getItemCount().toString())
//                        rvAdapter.notifyDataSetChanged()
                    }


                } else {
                    Log.e("Error", response.errorBody().toString())
//                    Toast.makeText(
//                        this@ParkinglotListFragment.context,
//                        response.errorBody().toString(),
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }catch (Ex:Exception){
                Ex.localizedMessage?.let { Log.e("Error", it) }
            }
        }
    }

    external fun deleteParkinglotFromJNI(id: String): Int

    fun deleteParkinglot(d: DataParkinglot) {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val id = PlaceholderContent.itemToId(d)
                val response = apiInterface.deleteParkinglot(id)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("Parkinglot: ", response.body().toString())

//                    val rd: DataParkinglot? = response.body()?.data
//                    if (rd != null) {
////                        PlaceholderContent.removeItem(position)
//                    }

                } else {
                    Log.e("Error", response.errorBody().toString())
//                    Toast.makeText(
//                        this@ParkinglotEditFragment.context,
//                        response.errorBody().toString(),
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }catch (Ex:Exception){
                Ex.localizedMessage?.let { Log.e("Error", it) }
            }
        }
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_EDIT_BACK = "edit-back"
        const val ARG_EDIT_POSITION = "edit-position"

        @JvmStatic
        fun newInstance(columnCount: Int, editBack: Int, editPosition: Int) =
            ParkinglotListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putInt(ARG_EDIT_BACK, editBack)
                    putInt(ARG_EDIT_POSITION, editPosition)
                }
            }
    }
}