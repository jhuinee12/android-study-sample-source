package com.zahi.jsoupscrapingsample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zahi.jsoupscrapingsample.databinding.ActivityLotteryBinding
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class LotteryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLotteryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLotteryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            doTask("https://www.dhlottery.co.kr/gameResult.do?method=byWin&wiselog=C_A_1_2")
        }
    }

    @SuppressLint("CheckResult")
    private fun doTask(url : String) {
        var documentTitle = ""
        val itemList: ArrayList<LotteryItem> = arrayListOf() //MovieItem 배열

        Single.fromCallable {
            try {
                val doc = Jsoup.connect(url).get()

                val elements : Elements = doc.select("table tbody tr")
                run elemLoop@{
                    elements.forEach{
                        val element = it.select("td")
                        val rank = element[0].text()
                        val sumPrize = element[1].text()
                        val people = element[2].text()
                        val prize = element[3].text()

                        val item = LotteryItem(rank, sumPrize, people, prize)
                        itemList.add(item)
                    }
                }

                documentTitle = doc.title()
            } catch (e : Exception) {e.printStackTrace()}

            return@fromCallable documentTitle   // subscribe 호출
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    itemList.forEach {
                        showData(it.toString())
                    }
                },
                { it.printStackTrace() })
    }

    private fun showData(msg : String) {
        binding.output.append(msg + "\n\n")
    }
}