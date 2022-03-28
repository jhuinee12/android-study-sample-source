package com.example.ks20220302_googlefitapi

import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ks20220302_googlefitapi.databinding.FragmentHistoryBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessStatusCodes
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataDeleteRequest
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task
import java.text.DateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit


const val HIST_TAG = "BasicHistoryApi"

class HistoryFragment : Fragment() {

    private var mBinding: FragmentHistoryBinding? = null
    private val binding get()                    = mBinding!!

    private val dateFormat = DateFormat.getDateInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHistoryBinding.inflate(inflater, container, false)

        binding.btn01.setOnClickListener { insertAndReadData() }
        binding.btn02.setOnClickListener { updateAndReadData() }
        binding.btn03.setOnClickListener { deleteData() }

        return binding.root
    }

    // <editor-fold desc="데이터 집계">

    private fun readHistoryData(): Task<DataReadResponse> {
        return Fitness.getHistoryClient(
            requireContext(),
            (activity as MainActivity).getGoogleAccount()
        )
            .readData(queryFitnessData())   // DataReadRequest // 사용자의 Google 피트니스 기록에서 데이터를 읽음
            .addOnSuccessListener { dataReadResponse ->
                printData(dataReadResponse)
            }
            .addOnFailureListener { e ->
                val msg = "데이터 불러오는 데 문제가 발생했습니다."
                Log.e(HIST_TAG, msg, e)
                binding.tvErr.text          = msg
                binding.tvErr.visibility    = View.VISIBLE
                binding.tvSucc.visibility   = View.GONE
            }
    }

    private fun queryFitnessData(): DataReadRequest {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val now = Date()
        calendar.time = now
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.WEEK_OF_YEAR, -1)
        val startTime = calendar.timeInMillis

        val msg = "Range Start: ${dateFormat.format(startTime)}\t||\tRange End: ${dateFormat.format(
            endTime
        )}"

        Log.i(HIST_TAG, msg)
        binding.tvLog.text = msg

        return DataReadRequest.Builder()
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
            .aggregate(DataType.AGGREGATE_STEP_COUNT_DELTA)
            .build()
    }
    private fun printData(dataReadResult: DataReadResponse) {
        if (dataReadResult.buckets.isNotEmpty()) {
            Log.i(
                HIST_TAG,
                "Number of returned buckets of DataSets is: " + dataReadResult.buckets.size
            )
            for (bucket in dataReadResult.buckets) {
                bucket.dataSets.forEach { dumpDataSet(it) }
            }
        } else if (dataReadResult.dataSets.isNotEmpty()) {
            Log.i(HIST_TAG, "Number of returned DataSets is: " + dataReadResult.dataSets.size)
            dataReadResult.dataSets.forEach { dumpDataSet(it) }
        }
    }

    private fun dumpDataSet(dataSet: DataSet) {
        Log.i(HIST_TAG, "Data returned for Data type: ${dataSet.dataType.name}")

        for (dp in dataSet.dataPoints) {
            Log.i(HIST_TAG, "Data point:")
            Log.i(HIST_TAG, "\tType: ${dp.dataType.name}")
            Log.i(HIST_TAG, "\tStart: ${dp.getStartTimeString()}")
            Log.i(HIST_TAG, "\tEnd: ${dp.getEndTimeString()}")
            dp.dataType.fields.forEach {
                Log.i(HIST_TAG, "\tField: ${it.name} Value: ${dp.getValue(it)}")
            }
        }
    }

    private fun DataPoint.getStartTimeString() = Instant.ofEpochSecond(this.getStartTime(TimeUnit.SECONDS))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().toString()

    private fun DataPoint.getEndTimeString() = Instant.ofEpochSecond(this.getEndTime(TimeUnit.SECONDS))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().toString()

    // </editor-fold>

    // <editor-fold desc = "데이터 삽입">

    private fun insertAndReadData() = insertData().continueWith { readHistoryData() }

    private fun insertData(): Task<Void> {
        val dataSet = insertFitnessData()
        var msg     = "History API Dataset에 Insert"
        Log.i(HIST_TAG, msg)

        return Fitness.getHistoryClient(
            requireContext(),
            (activity as MainActivity).getGoogleAccount()
        )
            .insertData(dataSet)
            .addOnSuccessListener {
                msg += "\n데이터 삽입 성공!"
                Log.i(HIST_TAG, msg)
                binding.tvSucc.text = msg
                binding.tvSucc.visibility = View.VISIBLE
                binding.tvErr.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                msg += "\n데이터 삽입 실패"
                Log.e(HIST_TAG, msg, exception)
                binding.tvErr.text = msg
                binding.tvErr.visibility = View.VISIBLE
                binding.tvSucc.visibility = View.GONE
            }
    }

    private fun insertFitnessData(): DataSet {
        Log.i(HIST_TAG, "새 데이터 삽입 리퀘스트 생성")

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val now = Date()
        calendar.time = now
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.HOUR_OF_DAY, -1)
        val startTime = calendar.timeInMillis

        val dataSource = DataSource.Builder()
            .setAppPackageName(requireContext())
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setStreamName("$HIST_TAG - step count")
            .setType(DataSource.TYPE_RAW)
            .build()

        val stepCountDelta = 950
        return DataSet.builder(dataSource)
            .add(
                DataPoint.builder(dataSource)
                    .setField(Field.FIELD_STEPS, stepCountDelta)
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build()
            ).build()
    }
    // </editor-fold>

    // <editor-fold desc = "데이터 수정">
    private fun updateAndReadData() = updateData().continueWithTask { readHistoryData() }

    private fun updateData(): Task<Void> {
        val dataSet = updateFitnessData()
        val startTime = dataSet.dataPoints[0].getStartTime(TimeUnit.MILLISECONDS)
        val endTime = dataSet.dataPoints[0].getEndTime(TimeUnit.MILLISECONDS)

        var msg     = "History API Dataset에 Update"
        Log.i(HIST_TAG, msg)

        val request = DataUpdateRequest.Builder()
            .setDataSet(dataSet)
            .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        return Fitness.getHistoryClient(
            requireContext(),
            (activity as MainActivity).getGoogleAccount()
        )
            .updateData(request)
            .addOnSuccessListener {
                msg += "\n데이터 수정 성공!"
                Log.i(HIST_TAG, msg)
                binding.tvSucc.text = msg
                binding.tvSucc.visibility = View.VISIBLE
                binding.tvErr.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                msg += "\n데이터 수정 실패"
                Log.e(HIST_TAG, msg, exception)
                binding.tvErr.text = msg
                binding.tvErr.visibility = View.VISIBLE
                binding.tvSucc.visibility = View.GONE
            }
    }

    private fun updateFitnessData(): DataSet {
        Log.i(HIST_TAG, "새 데이터 수정 리퀘스트 생성")

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val now = Date()
        calendar.time = now
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.MINUTE, -50)
        val startTime = calendar.timeInMillis

        val dataSource = DataSource.Builder()
            .setAppPackageName(requireContext())
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setStreamName("$HIST_TAG - step count")
            .setType(DataSource.TYPE_RAW)
            .build()

        val stepCountDelta = 1000
        return DataSet.builder(dataSource)
            .add(
                DataPoint.builder(dataSource)
                    .setField(Field.FIELD_STEPS, stepCountDelta)
                    .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build()
            ).build()
    }
    // </editor-fold>

    // <editor-fold desc="데이터 삭제">
    private fun deleteData() {
        var msg     = "오늘 걸음수 삭제"
        Log.i(HIST_TAG, msg)

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val now = Date()
        calendar.time = now
        val endTime = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val startTime = calendar.timeInMillis

        val request = DataDeleteRequest.Builder()
            .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .build()

        Fitness.getHistoryClient(requireContext(), (activity as MainActivity).getGoogleAccount())
            .deleteData(request)
            .addOnSuccessListener {
                msg += "\n오늘 걸음수 삭제 성공!"
                Log.i(HIST_TAG, msg)
                binding.tvSucc.text = msg
                binding.tvSucc.visibility = View.VISIBLE
                binding.tvErr.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                msg += "\n오늘 걸음수 삭제 실패"
                Log.e(HIST_TAG, msg, exception)
                binding.tvErr.text = msg
                binding.tvErr.visibility = View.VISIBLE
                binding.tvSucc.visibility = View.GONE
            }
    }
    // </editor-fold>
}