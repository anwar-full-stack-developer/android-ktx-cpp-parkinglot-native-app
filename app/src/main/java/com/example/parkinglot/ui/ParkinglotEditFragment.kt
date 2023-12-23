package com.example.parkinglot

import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.parkinglot.R
import com.example.parkinglot.data.ApiInterface
import com.example.parkinglot.data.DataParkinglot
import com.example.parkinglot.data.NewRequestDataParkinglot
import com.example.parkinglot.data.ResponseParkinglot
import com.example.parkinglot.data.RetrofitClient
import com.example.parkinglot.placeholder.PlaceholderContent
import com.google.gson.Gson

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private const val ARG_PARKINGLOT_ACTION_TYPE = "ARG_PARKINGLOT_ACTION_TYPE"
private const val ARG_PARKINGLOT_POSITION = "ARG_PARKINGLOT_POSITION"
private const val ARG_PARKINGLOT_ITEM_DATA = "ARG_PARKINGLOT_ITEM_DATA"

/**
 * A simple [Fragment] subclass.
 * Use the [ParkinglotEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParkinglotEditFragment : Fragment() {
    private var actionType: String? = null // EDIT | NEW
    private var position: Int = -1

    lateinit var dItemData: DataParkinglot
    lateinit var rvData: PlaceholderContent

    lateinit var editParkinglotTokenNumber: EditText
    lateinit var editParkinglotType: EditText
    lateinit var editParkinglotSize: EditText
    lateinit var editParkinglotWeight: EditText
    lateinit var editParkinglotBookingTime: EditText
    lateinit var editParkinglotBookingTimeTo: EditText
    lateinit var editParkinglotBookingStatus: EditText
    lateinit var editParkinglotPrice: EditText
    lateinit var editParkinglotStatus: EditText

    lateinit var editParkinglotSaveBtn : Button
    lateinit var editParkinglotCancelBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rvData = PlaceholderContent

        arguments?.let {
            actionType = it.getString(ARG_PARKINGLOT_ACTION_TYPE)
            position = it.getInt(ARG_PARKINGLOT_POSITION)
//            dItemData = it.getSerializable(ARG_PARKINGLOT_ITEM_DATA)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parkinglot_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Parkinglot", "POSITION "+ position)

        if (position > -1)
            dItemData = rvData.getItemData(position)!!

        editParkinglotTokenNumber = view.findViewById(R.id.editParkinglotTokenNumber)
        editParkinglotType = view.findViewById(R.id.editParkinglotType)
        editParkinglotSize = view.findViewById(R.id.editParkinglotSize)
        editParkinglotWeight = view.findViewById(R.id.editParkinglotWeight)
        editParkinglotBookingTime = view.findViewById(R.id.editParkinglotBookingTime)
        editParkinglotBookingTimeTo = view.findViewById(R.id.editParkinglotBookingTimeTo)
        editParkinglotBookingStatus = view.findViewById(R.id.editParkinglotBookingStatus)
        editParkinglotPrice = view.findViewById(R.id.editParkinglotPrice)
        editParkinglotStatus = view.findViewById(R.id.editParkinglotStatus)

        editParkinglotSaveBtn = view.findViewById(R.id.editParkinglotSaveBtn)
        editParkinglotCancelBtn = view.findViewById(R.id.editParkinglotCancelBtn)

        if (actionType == "EDIT") {
            editParkinglotTokenNumber.setText(dItemData!!.token_number)
            editParkinglotType.setText(dItemData!!.type)
            editParkinglotSize.setText(dItemData!!.size)
            editParkinglotWeight.setText(dItemData!!.weight)
            editParkinglotBookingTime.setText(dItemData!!.booking_time)
            editParkinglotBookingTimeTo.setText(dItemData!!.booking_time_to)
            editParkinglotBookingStatus.setText(dItemData!!.booking_status)
            editParkinglotPrice.setText(dItemData!!.price)
            editParkinglotStatus.setText(dItemData!!.status)

            editParkinglotSaveBtn.setText("Update")
        }
        else {
            editParkinglotSaveBtn.setText("Save")
        }
        editParkinglotSaveBtn.setOnClickListener(View.OnClickListener {
            Log.d("Parkinglot", "Submit Button Clicked. position: "+ position)

//            dItemData.copy()
            if (actionType == "EDIT") {
                dItemData?.token_number = editParkinglotTokenNumber.getText().toString()
                dItemData?.type = editParkinglotType.getText().toString()
                dItemData?.size = editParkinglotSize.getText().toString()
                dItemData?.weight = editParkinglotWeight.getText().toString()
                dItemData?.booking_time = editParkinglotBookingTime.getText().toString()
                dItemData?.booking_time_to = editParkinglotBookingTimeTo.getText().toString()
                dItemData?.booking_status = editParkinglotBookingStatus.getText().toString()
                dItemData?.price = editParkinglotPrice.getText().toString()
                dItemData?.status = editParkinglotStatus.getText().toString()

                PlaceholderContent.updateItem(position, dItemData)
//                updateParkinglot(dItemData)

                var gson = Gson()
                val ijstr = gson.toJson(dItemData)
                Log.d("Parkinglot: ", "Sending JSON data: " + ijstr)
                val response = updateParkinglotFromJNI(PlaceholderContent.itemToId(dItemData), ijstr)
                Log.d("Parkinglot: ", "Response JSON data: " + response)

                var rdj = gson?.fromJson(response, ResponseParkinglot::class.java)
                val rd: DataParkinglot? = rdj?.data
                if (rd != null) {
                    PlaceholderContent.addItem(rd)
                }
                goBackToList()
            } else {
                val dItemData : NewRequestDataParkinglot = NewRequestDataParkinglot("","","", "", "", "", "", "", "", )
                dItemData?.token_number = editParkinglotTokenNumber.getText().toString()
                dItemData?.type = editParkinglotType.getText().toString()
                dItemData?.size = editParkinglotSize.getText().toString()
                dItemData?.weight = editParkinglotWeight.getText().toString()
                dItemData?.booking_time = editParkinglotBookingTime.getText().toString()
                dItemData?.booking_time_to = editParkinglotBookingTimeTo.getText().toString()
                dItemData?.booking_status = editParkinglotBookingStatus.getText().toString()
                dItemData?.price = editParkinglotPrice.getText().toString()
                dItemData?.status = editParkinglotStatus.getText().toString()
//                saveParkinglot(dItemData)

                var gson = Gson()
                val ijstr = gson.toJson(dItemData)
                Log.d("Parkinglot: ", "Sending JSON data: " + ijstr)
                val response = saveParkinglotFromJNI(ijstr)
                Log.d("Parkinglot: ", "Response JSON data: " + response)

                var rdj = gson?.fromJson(response, ResponseParkinglot::class.java)
                val rd: DataParkinglot? = rdj?.data
                if (rd != null) {
                    PlaceholderContent.addItem(rd)
                }
                goBackToList()

            }
        })

        editParkinglotCancelBtn.setOnClickListener(View.OnClickListener {
            goBackToList()
        })

    }
    private fun goBackToList(){

        parentFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, ParkinglotListFragment.newInstance(1,1, position))
            .commit()
    }


    private external fun saveParkinglotFromJNI(d: String): String
    private external fun updateParkinglotFromJNI(id: String, d: String): String


    private fun saveParkinglot(d: NewRequestDataParkinglot) {
        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {

                val response = apiInterface.saveParkinglot(d)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("Parkinglot: ", "SAve Success: " +response.body().toString())

                    val rd: DataParkinglot? = response.body()?.data
                    if (rd != null) {
                        PlaceholderContent.addItem(rd)
                    }
                    goBackToList()
                } else {
                    Log.e("Error", "Save Error" + response.errorBody().toString())
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

    private fun updateParkinglot(d: DataParkinglot) {

        val retrofit = RetrofitClient.getInstance()
        val apiInterface = retrofit.create(ApiInterface::class.java)
        GlobalScope.launch {
            try {
                val id = PlaceholderContent.itemToId(d)
                val response = apiInterface.updateParkinglot(id, d)
                if (response.isSuccessful()) {
                    //your code for handling success response
                    Log.d("Parkinglot: ", response.body().toString())

                    val rd: DataParkinglot? = response.body()?.data
//                    if (rd != null) {
////                        PlaceholderContent.addItem(rd)
//                    }
                    goBackToList()
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParkinglotEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(actionType: String, position: Int) =
            ParkinglotEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARKINGLOT_ACTION_TYPE, actionType)
                    putInt(ARG_PARKINGLOT_POSITION, position)
                }
            }
    }
}