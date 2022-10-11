package com.zahi.jsoupscrapingsample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zahi.jsoupscrapingsample.databinding.ActivityMainBinding
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.select.Elements

/**
 * 참고 url : https://min-wachya.tistory.com/131
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener { // 크롤링 시작
            doTask("https://movie.naver.com/movie/running/current.nhn?order?reverse")
        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, LotteryActivity::class.java))
        }
    }

    // 크롤링 하기
    @SuppressLint("CheckResult")
    private fun doTask(url : String) {
        var documentTitle = ""
        val itemList: ArrayList<MovieItem> = arrayListOf() //MovieItem 배열

        Single.fromCallable {
            try {
                // 사이트에 접속해서 HTML 문서 가져오기
                val doc = Jsoup.connect(url).get()

                // HTML 파싱해서 데이터 추출하기
                // ul.lst_detail_t1아래의 li 태그만 가져오기
                val elements : Elements = doc.select("ul.lst_detail_t1 li")
                // (여러개의) elements 처리
                run elemLoop@{
                    elements.forEachIndexed{ index, elem ->
                        // elem은 하나의 li를 전달해줌
                        val title = elem.select("dt.tit a").text()  // <dt class="tit">의 <a> 태그 text 가져옴
                        val num = elem.select("dl.info_star div.star_t1 span.num").text()
                        val num2 = elem.select("span.num2").text()
                        val reserve = elem.select("dl.info_exp div.star_t1 span.num").text()

                        // MovieItem 아이템 생성 후 추가
                        val item = MovieItem(title, num, num2, reserve)
                        itemList.add(item)

                        // 10개만 가져오기
                        if (index == 9) return@elemLoop
                    }
                }

                // 올바르게 HTMl 문서 가져왔다면 title로 바꾸기
                documentTitle = doc.title()
            } catch (e : Exception) {e.printStackTrace()}

            return@fromCallable documentTitle   // subscribe 호출
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                // documentTitle 응답 성공 시
                { text ->
                    // TextView에 출력하기
                    showData(text.toString())

                    // itemList 출력하기
//                    showData(itemList.joinToString())
                    itemList.forEach {
                        showData(it.toString())
                    }
                },
                // documentTitle 응답 오류 시
                { it.printStackTrace() })
    }

    // TextView에 출력하기
    private fun showData(msg : String) {
        binding.output.append(msg + "\n\n")
    }
}

/*
html

<ul class="lst_detail_t1">
	<li>..</li>
	<li>
		<div class="thumb">
			<a href="/movie/bi/mi/basic.naver?code=220044">
				<img src="https://movie-phinf.pstatic.net/20220906_136/1662449769362fWpdE_JPEG/movie_image.jpg?type=m99_141_2"
					alt="극장판 검정고무신 : 즐거운 나의 집"
					onerror="this.src='https://ssl.pstatic.net/static/movie/2012/09/dft_img99x141.png'">
			</a><!-- N=a:nol.img,r:2,i:220044 -->
		</div>
		<dl class="lst_dsc">
			<dt class="tit">
				<span class="ico_rating_all">전체 관람가</span>
				<a href="/movie/bi/mi/basic.naver?code=220044">극장판 검정고무신 : 즐거운 나의 집</a>
				<!-- N=a:nol.title,r:2,i:220044 -->
			</dt>
			<dd class="star">
				<dl class="info_star">
					<dt class="tit_t1">네티즌</dt>
					<dd>
						<div class="star_t1">
							<a href="/movie/bi/mi/point.naver?code=220044#pointAfterTab"><span class="st_off"><span class="st_on" style="width:83.5%"></span></span><span class="num">8.35</span><span class="num2">참여 <em>68</em>명</span></a><!-- N=a:nol.urating -->
						</div>
						<span class="split">|</span>
					</dd>
					<dt class="tit_t2">기자 · 평론가</dt>
					<dd>
						<div class="star_t1">
							<a href="/movie/bi/mi/point.naver?code=220044#pointExpertTab"><span class="st_off"><span class="st_on" style="width:70.0%"></span></span><span class="num">7.00</span><span class="num2">참여 <em>1</em>명</span></a><!-- N=a:nol.crating -->
						</div>
					</dd>
				</dl>
			</dd>
			<dd>
				<dl class="info_txt1">
				<dt class="tit_t1">개요</dt>
				<dd>
					<span class="link_txt">
						<a href="/movie/sdb/browsing/bmovie.naver?genre=15">애니메이션</a><!-- N=a:nol.genre,r:1 -->
					</span>
					<span class="split">|</span>
					79분
					<span class="split">|</span>
					2022.10.06 개봉
				</dd>
				<dt class="tit_t2">감독</dt>
				<dd>
					<span class="link_txt">
						<a href="/movie/bi/pi/basic.naver?code=9805">송정율</a><!-- N=a:nol.director,r:1 -->,
						<a href="/movie/bi/pi/basic.naver?code=444578">송요한</a><!-- N=a:nol.director,r:2 -->
					</span>
				</dd>
				<dt class="tit_t3">출연</dt>
				<dd>
					<span class="link_txt">
						<a href="/movie/bi/pi/basic.naver?code=176854">박지윤</a><!-- N=a:nol.actor,r:1 -->,
						<a href="/movie/bi/pi/basic.naver?code=279311">오인실</a><!-- N=a:nol.actor,r:2 -->
					</span>
				</dd>
				</dl>
			</dd>

			<dd class="info_t1">
				<div class="btn_area">
					<a href="/movie/bi/mi/running.naver?code=220044" class="btn_rsv">예매하기</a><!-- N=a:nol.ticket,r:2,i:220044 -->
					<span class="btn_t1">
						<a href="/movie/bi/mi/photoView.naver?code=220044" class="item1">포토보기</a><!-- N=a:nol.photo,r:2,i:220044 -->
						<a href="/movie/bi/mi/mediaView.naver?code=220044&amp;mid=53255#tab" class="item2">예고편</a><!-- N=a:nol.trailer,r:2,i:220044 -->
					</span>
				</div>
			</dd>
		</dl>
	</li>
	<li>..</li>
	<li>..</li>
	<li>..</li>
	..
</ul>
 */