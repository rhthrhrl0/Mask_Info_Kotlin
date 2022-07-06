package com.example.mask_info_kotlin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mask_info_kotlin.databinding.ItemStoreBinding
import com.example.mask_info_kotlin.model.Store

//아이템 뷰 정보를 가지고 있는 클래스임.
class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding=ItemStoreBinding.bind(itemView) //아이템뷰를 뷰홀더와 바인딩시켜줌.
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
        holder.binding.storeItem=store //데이터바인딩을 위해 연결.
    }

    override fun getItemCount(): Int = mItems.size

    //외부에서 새로운 데이터를 주입해서 교체시킴.
    fun updateItems(mItems: List<Store>) {
        this.mItems = mItems
        notifyDataSetChanged() //UI 갱신
    }

}

@BindingAdapter("app:remainStatText")
fun setRemainStatText(textView: TextView, store: Store) {
    var remainStat = "충분"
    when (store.remain_stat) {
        "plenty" -> textView.text = "충분"
        "some" -> textView.text = "여유"
        "few" -> textView.text = "매진 임박"
        "empty" -> textView.text = "재고 없음"
        else -> { }
    }
}

@BindingAdapter("app:countText")
fun setCountText(textView: TextView, store: Store) {
    when (store.remain_stat) {
        "plenty" -> textView.text = "100개 이상"
        "some" -> textView.text = "30개 이상"
        "few" -> textView.text = "2개 이상"
        "empty" -> textView.text = "1개 이하"
        else -> { }
    }
}

@BindingAdapter("app:textColor")
fun setColorText(textView: TextView, store: Store) {
    when (store.remain_stat) {
        "plenty" -> textView.setTextColor(Color.GREEN)
        "some" -> textView.setTextColor(Color.YELLOW)
        "few" -> textView.setTextColor(Color.RED)
        "empty" -> textView.setTextColor(Color.GRAY)
        else -> { }
    }
}

@BindingAdapter("app:distanceText")
fun setDistanceText(textView: TextView, store: Store) {
    textView.text = String.format("%.2fkm", store.distance)
}