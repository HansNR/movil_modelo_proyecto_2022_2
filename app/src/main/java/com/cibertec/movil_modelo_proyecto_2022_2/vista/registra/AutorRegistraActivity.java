package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Grado;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceAutor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceGrado;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutorRegistraActivity extends NewAppCompatActivity {
    Spinner spnGrado;
    ArrayAdapter<String> adaptador;
    ArrayList<String> grados = new ArrayList<String>();

    //Servicio
    ServiceAutor serviceAutor;
    ServiceGrado serviceGrado;

    //componentes de la GUI
    EditText txtId, txtNom, txtApe, txtFec, txtTel,txtFre, txtestado, txtgrado;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_autor_registra);

        txtId = findViewById(R.id.txtRegEdiIdautor);
        txtNom = findViewById(R.id.txtRegEdiNombre);
        txtApe = findViewById(R.id.txtRegEdiApellido);
        txtFec = findViewById(R.id.txtRegEdiFechanaciemnto);
        txtTel = findViewById(R.id.txtRegEdiTelefono);
        txtFre = findViewById(R.id.txtRegEdiFechaRegistro);
        txtestado = findViewById(R.id.txtRegEdiEstado);
        txtgrado = findViewById(R.id.txtRegEdiGrado);

        btnRegistrar = findViewById(R.id.btnRegEdiEnviar);
        serviceGrado = ConnectionRest.getConnection().create(ServiceGrado.class);

        //para el adaptador
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, grados);

        spnGrado.setAdapter(adaptador);

        cargaGrado();

    }
    public void cargaGrado(){
        Call<List<Grado>> call = serviceGrado.Todos();
        call.enqueue(new Callback<List<Grado>>() {
            @Override
            public void onResponse(Call<List<Grado>> call, Response<List<Grado>> response) {
                if (response.isSuccessful()){
                    List<Grado> lstGrados =  response.body();
                    for(Grado obj: lstGrados){
                        grados.add(obj.getIdGrado() +":"+ obj.getDescripcion());
                    }
                    adaptador.notifyDataSetChanged();
                }else{
                    mensajeToast("Error al registrar al Servicio Rest >>> ");
                }
            }

            @Override
            public void onFailure(Call<List<Grado>> call, Throwable t) {
                mensajeToast("Error al registrar Servicio Rest >>> " + t.getMessage());
            }
        });
    }

    public void mensajeToast(String mensaje){
        Toast toast1 =  Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }

    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

}
