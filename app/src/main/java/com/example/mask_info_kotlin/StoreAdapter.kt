package com.example.mask_info_kotlin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mask_info_kotlin.model.Store

//아이템 뷰 정보를 가지고 있는 클래스임.
class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // 뷰홀더의 멤버변수들.
    var nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
    var addressTextView: TextView = itemView.findViewById(R.id.addr_text_view)
    var distanceTextView: TextView = itemView.findViewById(R.id.distance_text_view)
    var remainTextView: TextView = itemView.findViewById(R.id.remain_text_view)
    var countTextView: TextView = itemView.findViewById(R.id.count_text_view)
}

//internal은 protected같은거임
class StoreAdapter : RecyclerView.Adapter<StoreViewHolder>() {
    private var mItems: List<Store> = ArrayList<Store>() //초반에 그냥 초기화 해놓는게 널에러 안나고 좋음.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder {
        //안드로이드의 모든 뷰 및 뷰그룹에서 자체적으로 컨텍스트를 얻을 수 있다. 이 리사이클러뷰가 붙을 뷰그룹의 컨텍스트를 얻음.
        // 이건 그냥 외우면 됨. 지정한 레이아웃 대로 인플레이트 시킬건데, parent 에게 올릴 뷰임. 근데 인플레이트 시킬때 붙여서 나오지는 않게
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store, parent, false)

        // 인플레이트 된 view를 지닌 뷰홀더를 생성해서 리턴함.
        return StoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val store: Store = mItems[position] //뷰홀더와 연결시킬 Store를 얻음.
        holder.nameTextView.text = store.name //뷰홀더와 연결된 자식뷰에 값을 설정.
        holder.addressTextView.text = store.addr
        holder.distanceTextView.text = String.format("%.2fkm",store.distance)
        var remainStat = "충분"
        var count = "100개 이상" //디폴트 값.
        var color = Color.GREEN
        when (store.remain_stat) {
            "plenty" -> {
                count = "100개 이상"
                remainStat = "충분"
                color = Color.GREEN
            }
            "some" -> {
                count = "30개 이상"
                remainStat = "여유"
                color = Color.YELLOW
            }
            "few" -> {
                count = "2개 이상"
                remainStat = "매진 임박"
                color = Color.RED
            }
            "empty" -> {
                count = "1개 이하"
                remainStat = "재고 없음"
                color = Color.GRAY
            }
            else -> {
            }
        }
        holder.remainTextView.text = remainStat
        holder.countTextView.text = count
        holder.remainTextView.setTextColor(color)
        holder.countTextView.setTextColor(color)
    }

    override fun getItemCount(): Int = mItems.size

    //외부에서 새로운 데이터를 주입해서 교체시킴.
    fun updateItems(mItems: List<Store>) {
        this.mItems = mItems
        notifyDataSetChanged() //UI 갱신
    }

}