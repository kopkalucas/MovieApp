package com.example.movieapp1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment() {

    private val fbAuth = FirebaseAuth.getInstance()
    private val LOG_DEUBG = "LOG_DEBUG"

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //jak kline w zarejestruj to przejdz do fragmentu zaloguj
        view.findViewById<Button>(R.id.zarejestruj1).apply {
            setOnClickListener {
                view.findNavController().navigate(R.id.registrationFragment)
            }
        }
        view.findViewById<Button>(R.id.zaloguj1).apply {
            setOnClickListener {
                val email = view.findViewById<TextInputEditText>(R.id.email1).text?.trim().toString()
                val pass =  view.findViewById<TextInputEditText>(R.id.haslo1).text?.trim().toString()
                if(email!=""&&pass!=""){//zabezpieczenie
                    //logowanie się przez podanie emaila i hasła
                    fbAuth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener { authRes ->
                            if(authRes.user != null) startApp()
                        }
                        .addOnFailureListener{ exc ->
                            Snackbar.make(requireView(), "Nie ma takiego emaila", Snackbar.LENGTH_SHORT)
                                .show()
                            //komunikat o błędzie
                            Log.d(LOG_DEUBG, exc.message.toString())
                        }
                }

            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}