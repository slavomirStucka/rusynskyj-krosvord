package com.mastercoding.myapplication.crossword

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.mastercoding.myapplication.R

class TranslationsAdapter (context: Context, private val gridItems:List<String>) : ArrayAdapter<String>(context,0,gridItems){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var itemView = convertView
        val holder: ViewHolder
        val currentItem= gridItems[position]

        //NAME OF COLUMNS
        if (position==0||position==1){
            if(convertView==null){
                itemView = LayoutInflater.from(context)
                    .inflate(R.layout.translation_nameofcolumns_layout,parent,false)

                holder = ViewHolder()
                holder.textView=itemView.findViewById(R.id.nameofcolumns_translation)

                itemView.tag=holder
            }else{
                holder=itemView?.tag as ViewHolder
            }
        //WORDS
        }else {
            if (position % 2 == 0) {
                if(convertView==null){
                    itemView = LayoutInflater.from(context)
                        .inflate(R.layout.translation_words_layout,parent,false)

                    holder = ViewHolder()
                    holder.textView=itemView.findViewById(R.id.rusyn_text_translation)

                    itemView.tag=holder
                }else{
                    holder=itemView?.tag as ViewHolder
                }
            }

            else{
                if(convertView==null){
                    itemView = LayoutInflater.from(context)
                        .inflate(R.layout.translation_words_layout,parent,false)

                    holder = ViewHolder()
                    holder.textView=itemView.findViewById(R.id.rusyn_text_translation)
                    holder.textView.setTextColor(ContextCompat.getColor(context, R.color.yellow))
                    holder.textView.setTypeface(null, Typeface.NORMAL);

                    itemView.tag=holder
                }else{
                    holder=itemView?.tag as ViewHolder
                }
            }
        }


        holder.textView.text = currentItem



        return itemView!!
    }

    private class ViewHolder{
        lateinit var textView: TextView
    }

}