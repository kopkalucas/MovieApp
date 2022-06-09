package com.example.movieapp1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
class Adapter(private val dataArray: ArrayList<DatabaseRow>):RecyclerView.Adapter<Adapter.MyViewHolder>() {
    private lateinit var myRef: DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.MyViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val view=inflater.inflate(R.layout.database_row,parent,false)
    return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    override fun onBindViewHolder(holder: Adapter.MyViewHolder, position: Int) {
holder.tytul.setText(dataArray[holder.adapterPosition].tytul)
        holder.widziane.setText(dataArray[holder.adapterPosition].widziane)
        //jak przycisne usun w liscie to usunie dany rekord
        holder.usun.setOnClickListener{
            var uid=auth.currentUser?.uid.toString()
            val firebase= FirebaseDatabase.getInstance()
            myRef=firebase.getReference(uid)
            var tekst=ListaRekordowDoModyfikacji[position].toString()
            var b=tekst.indexOf("tytul")+6
            var c=tekst[b].toString()
            for(i in 0..100){
                b++
                if(tekst[b]==')'){
                    break;
                }else{
                    c=c+tekst[b]
                }

            }
            //tu usuwam rekord o tytule c w firebase
                        myRef.child(c).removeValue()
        }
    }
    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tytul = view.findViewById<TextView>(R.id.tytul)
        val widziane = view.findViewById<TextView>(R.id.widziane)
        val usun = view.findViewById<Button>(R.id.usun)
    }
}