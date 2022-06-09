package com.example.movieapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*
import kotlin.collections.ArrayList
//zmienne globalne
var CoWybrales=""
var ListaRekordowDoModyfikacji=ArrayList<DatabaseRow>()
var wielkosc=0
var wielkosc2=0

class MainActivity2 : AppCompatActivity() {

    private lateinit var myRef:DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var dodaj2: Button
    private lateinit var PokazCalaListe: Button
    private lateinit var WyborZRozwijanejListy: Button
    private lateinit var Szukaj: Button
    private lateinit var wpiszTytul: EditText
    private lateinit var poleSzukania: EditText
    private  lateinit var listOfItems:ArrayList<DatabaseRow>
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        dodaj2=findViewById(R.id.dodaj2)
        wpiszTytul=findViewById(R.id.wpiszTytul)
        spinner=findViewById(R.id.spinner)
        PokazCalaListe=findViewById(R.id.PokazCalaListe)
        WyborZRozwijanejListy=findViewById(R.id.WyborZRozwijanejListy)
        Szukaj=findViewById(R.id.szukaj)
        poleSzukania=findViewById(R.id.poleSzukania)


        val options= arrayOf("widziane","niewidziane")

        spinner.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                CoWybrales=options.get(p2)//opcja ktora wybralem
                dodanieDoAdapteraWszystkichElementow()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }



        var uid=auth.currentUser?.uid.toString()

        val firebase= FirebaseDatabase.getInstance()
        myRef=firebase.getReference(uid)

        recyclerView.layoutManager= GridLayoutManager(applicationContext, 1)

        dodaj2.setOnClickListener {
            val tytul=wpiszTytul.text.toString()
            if(tytul!=""){
                val widziane=CoWybrales
                val firebaseInput=DatabaseRow(widziane,tytul)
                myRef.child(tytul).setValue(firebaseInput)
            }
        }

        dodanieDoAdapteraWszystkichElementow()

        PokazCalaListe.setOnClickListener {
            dodanieDoAdapteraWszystkichElementow()
        }
        //zalezy od wybranej opcji w liscie rozwijanej, jak przycisniemy ten przycisk to sortuje po wybranej opcji liste
        WyborZRozwijanejListy.setOnClickListener {
            var wybor=""
            var index=0
            var tab= mutableListOf<Int>()
            for(i in ListaRekordowDoModyfikacji){

                var a=i.toString()
                for (j in 21..40) {
                    if(a[j]==','){//chcemy wycignac parametr widziane, wiec pobieramy tekst do ','
                        break;
                    }
                    wybor=wybor+a[j]
                }
                if(wybor==CoWybrales){
                    index++
                }else{
                    tab.add(index)
                    index++
                }
                wybor=""
            }
            //tab przechowuje wszystkie numery rekordow, ktore beda usuniete w adapterze na potrzeby podgladu
            var kkk=tab.size-1
            for(i in tab){

                            var cos=ListaRekordowDoModyfikacji[tab[kkk]]
            ListaRekordowDoModyfikacji.remove(cos)
                kkk--
            }
            //aktualizowanie, żeby zmienić rekordy w adapterze
            setupAdapter(ListaRekordowDoModyfikacji)
        }
        //jak wpisze w edittext obok szukaj i klikne szukaj to szuka rekordy z tytulem rownym temu edittext
        szukaj.setOnClickListener {
            wielkosc=ListaRekordowDoModyfikacji.size
if(wielkosc2!=wielkosc){
    //komunikat
    dodanieDoAdapteraWszystkichElementow()
    Toast.makeText(applicationContext, "Nacisnij jeszcze raz w przycisk szukaj!", Toast.LENGTH_SHORT).show()
}else{


    val poleSzukaniaStri=poleSzukania.text.toString()
    var l=poleSzukaniaStri.count()-1


            var index=0
            var tab= mutableListOf<Int>()

            for(i in ListaRekordowDoModyfikacji){

                var a=i.toString()
                var b=i.toString().indexOf("tytul")+6//indeks na ktorym sie zaczyna tytul(na potrzeby wyciagniecia tytulu ze stringa)
//szukam rekordow nie pasujacych do słowa klucz i je usuwam
                var hhhh=0
                    for (i in b..b+l) {
                        if(a[i]!=poleSzukaniaStri[hhhh]){
                            tab.add(index)
                            break;
                        }
                        hhhh++
                    }
                index++
            }
            var kkk=tab.size-1
            for(i in tab){

                var cos=ListaRekordowDoModyfikacji[tab[kkk]]
                ListaRekordowDoModyfikacji.remove(cos)
                kkk--
            }
    //aktualizowanie, żeby zmienić rekordy w adapterze
            setupAdapter(ListaRekordowDoModyfikacji)
        }}


    }
    private  fun dodanieDoAdapteraWszystkichElementow(){
        myRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listOfItems=ArrayList()
                for(i in dataSnapshot.children){
                    val newRow=i.getValue(DatabaseRow::class.java)
                    listOfItems.add(newRow!!)
                }
                ListaRekordowDoModyfikacji=listOfItems//lista wykorzytywana do modyfikacji

                wielkosc2=listOfItems.size

                setupAdapter(listOfItems)
            }

        })
    }
    //aktualizowanie adaptera
private  fun setupAdapter(arrayData:ArrayList<DatabaseRow>){
    recyclerView.adapter=Adapter(arrayData)
}





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logOut -> {
                auth.signOut()
                finish()
            }
        }
        return false
    }


}