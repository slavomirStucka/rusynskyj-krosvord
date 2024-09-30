package com.mastercoding.myapplication.crossword

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mastercoding.myapplication.R

class LetterAdapter (context:Context, private val gridItems:List<String>) : ArrayAdapter<String>(context,0,gridItems){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var itemView = convertView
        val holder: ViewHolder
        val currentItem= gridItems[position]

        if (currentItem==" "){
            if(convertView==null){
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.crossword_square_empty_layout,parent,false)

                holder = ViewHolder()
                holder.textView=itemView.findViewById(R.id.crossword_square_empty)
                //holder.textView.textSize=5F

                val screenWidthPixels = context.resources.displayMetrics.widthPixels
                val textSize = screenWidthPixels * 0.065f
                holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                itemView.tag=holder
            }else{
                holder=itemView?.tag as ViewHolder
            }

        }else{
            if(convertView==null){
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.crossword_square_layout,parent,false)
                if (currentItem!="*"){
                    itemView.background = ContextCompat.getDrawable(context,
                        R.drawable.rectanglefilled
                    )
                }else{
                    itemView.background = ContextCompat.getDrawable(context,
                        R.drawable.rectangleempty
                    )
                }
                holder = ViewHolder()
                holder.textView=itemView.findViewById(R.id.crossword_square)
                //holder.textView.textSize=45F

                val screenWidthPixels = context.resources.displayMetrics.widthPixels
                val textSize = screenWidthPixels * 0.065f
                holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
                itemView.tag=holder
            }else{
                holder=itemView?.tag as ViewHolder
            }
        }


        holder.textView.text = if (currentItem == "*") " " else currentItem



        return itemView!!
    }

    private class ViewHolder{
        lateinit var textView: TextView
    }



}