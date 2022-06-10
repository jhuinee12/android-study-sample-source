package com.example.ks20220302_googlefitapi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ks20220302_googlefitapi.databinding.FragmentRecordingBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataReadRequest
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


const val RECORD_TAG = "BasicRecordingApi"

class RecordingFragment : Fragment() {
    private var mBinding: FragmentRecordingBinding? = null
    private val binding get()                       = mBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = FragmentRecordingBinding.inflate(inflater, container, false)

        subscribe()

        binding.btnLiveLocation.setOnClickListener { subscribe() }
        binding.btnLiveStepCount.setOnClickListener { dumpSubscriptionsList() }
        binding.btnCancel.setOnClickListener { cancelSubscription() }
        binding.btnRead.setOnClickListener { readData() }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelSubscription()
    }

    /**
     * 구독 시작
     * :: 가능한 모든 데이터 소스에서 데이터 타입 구독 가능
     * :: 특정 데이터소스만 사용하는 것도 가능
     */
    private fun subscribe() {
        Fitness.getRecordingClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            .subscribe(DataType.TYPE_CALORIES_EXPENDED) // 소비된 칼로리 수
            //.subscribe(DataType.TYPE_STEP_COUNT_DELTA)  // 시간 간격 동안의 총 단계수??
            //.subscribe(DataType.TYPE_WEIGHT)            // 체중 kg
            .addOnSuccessListener {
                val msg = "구독 성공!"
                Log.i(RECORD_TAG, msg)
                binding.titleTextView.text = msg
            }
            .addOnFailureListener {
                val msg = "구독 실패"
                Log.i(RECORD_TAG, msg)
                binding.titleTextView.text = msg
            }

    }

    /**
     * 구독 목록 호출
     */
    private fun dumpSubscriptionsList() {
        Fitness.getRecordingClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            .listSubscriptions(DataType.TYPE_STEP_COUNT_DELTA)
//            .listSubscriptions(DataType.TYPE_CALORIES_EXPENDED)
            .addOnSuccessListener { subscriptions ->
                var msg = "dumpSubscriptionsList() 호출"
                for (subscription in subscriptions) {
                    val dataType = subscription.dataType!!
                    msg += "\n활성화 된 구독 data type: ${dataType.name}"
                    msg += "\n활성화 된 구독 data fields: ${dataType.fields[0]}"
                    Log.i(RECORD_TAG, "활성화 된 구독 data type: ${dataType.name}")
                }

                if (subscriptions.isEmpty()) {
                    msg += "\n활성화 된 구독이 없음"
                    Log.i(RECORD_TAG, "활성화 된 구독이 없음")
                }

                binding.titleTextView.text = msg
            }
    }

    /**
     * 구독 취소
     */
    private fun cancelSubscription() {
//        val dataTypeStr = DataType.TYPE_CALORIES_EXPENDED.toString()
        val dataTypeStr = DataType.TYPE_STEP_COUNT_DELTA.toString()
        Log.i(RECORD_TAG, "Unsubscribing from data type: $dataTypeStr")

        Fitness.getRecordingClient(requireContext(), (activity as MainActivity).getGoogleAccount())
//            .unsubscribe(DataType.TYPE_CALORIES_EXPENDED)
            .unsubscribe(DataType.TYPE_STEP_COUNT_DELTA)
            .addOnSuccessListener {
                val msg = "구독 취소 성공 - data type: $dataTypeStr"
                Log.i(RECORD_TAG, msg)
                binding.titleTextView.text = msg
            }
            .addOnFailureListener {
                val msg = "구독 취소 실패 - data type: $dataTypeStr"
                Log.i(RECORD_TAG, msg)
                binding.titleTextView.text = msg
            }

    }

    private fun readData() {
        var stepCount = 0

        val cal: Calendar = Calendar.getInstance()
        val now: Date = Calendar.getInstance().time
        cal.time = now

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        // 시작 시간
        cal.set(
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH), 6, 0, 0
        )
        val startTime: Long = cal.timeInMillis

        // 종료 시간
        cal.set(
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH), 22, 0, 0
        )
        val endTime: Long = cal.timeInMillis

        Fitness.getHistoryClient(
            requireContext(),
            GoogleSignIn.getLastSignedInAccount(requireContext())!!
        )
            .readData(
                DataReadRequest.Builder()
                    .read(DataType.TYPE_STEP_COUNT_DELTA) // Raw 걸음 수
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build()
            )
            .addOnSuccessListener { response ->
                val dataSet =
                    response.getDataSet(DataType.TYPE_STEP_COUNT_DELTA)
                Log.i(RECORD_TAG, "Data returned for Data type: " + dataSet.dataType.name)
                var readDataText = ""
                for (dp in dataSet.dataPoints) {
                    readDataText += "\nData point:\tType: " + dp.dataType.name +
                        "\nStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) +
                        "\nEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS))
                    Log.i(RECORD_TAG, readDataText)
                    for (field in dp.dataType.fields) {
                        val fieldValue = dp.getValue(field).toString().toInt()
                        stepCount += fieldValue
                        Log.i( RECORD_TAG, "\tField: " + field.name + " Value: " + fieldValue )
                        readDataText += "\nField: " + field.name + " Value: " + fieldValue + "\n\n"
                    }
                    binding.descTextView.text = readDataText
                    binding.titleTextView.text = "$stepCount 걸음"
                }
            }
    }
}