package com.cibertec.movil_modelo_proyecto_2022_2.vista.registra;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.cibertec.movil_modelo_proyecto_2022_2.R;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Pais;
import com.cibertec.movil_modelo_proyecto_2022_2.entity.Proveedor;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServicePais;
import com.cibertec.movil_modelo_proyecto_2022_2.service.ServiceProveedor;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ConnectionRest;
import com.cibertec.movil_modelo_proyecto_2022_2.util.FunctionUtil;
import com.cibertec.movil_modelo_proyecto_2022_2.util.NewAppCompatActivity;
import com.cibertec.movil_modelo_proyecto_2022_2.util.ValidacionUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProveedorRegistraActivity extends NewAppCompatActivity {


    //spiner(el espiner siempre se crea con 3 datos)
    private Spinner spnPais;
    private ArrayAdapter<String> adaptador;
    private List<String> lstpaises =new ArrayList<String>();

    private Button btnRegistrar;
    private EditText txtRazon;
    private EditText txtDireccion;
    private EditText txtTelefono;
    private EditText txtCelular;
    private EditText txtContacto;
    private EditText txtRUC;
    private TextView txtSalida;

    //Servicio Rest, lo invocamos
    //vamos a traer la conexion con el servicio rest
    private ServiceProveedor serproo;
    private ServicePais serpais;

    private List<Pais> lstPais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proveedor_registra);

        //vamos a recuperar
        //estamos relacionando el codigo html con el atributo creado
        //Para el adapatador
        spnPais = findViewById(R.id.spnPais);
        //este esta amarrado al arraylist
        adaptador = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, lstpaises);
        //al spiner le mando el adaptador, ya que este trabaja con ello
        spnPais.setAdapter(adaptador);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        txtRazon = findViewById(R.id.txtRazon);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCelular = findViewById(R.id.txtCelular);
        txtContacto = findViewById(R.id.txtContacto);
        txtRUC = findViewById(R.id.txtRUC);



        //aqui se obtene la conexion al servicio rest
        //lamamos a la conexion y la almacenamos en rest
        //aqui le pasamos el UserService
        serproo = ConnectionRest.getConnection().create(ServiceProveedor.class);
        serpais = ConnectionRest.getConnection().create(ServicePais.class);

        listarpaises();


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String raz = txtRazon.getText().toString();
                String dir = txtDireccion.getText().toString();
                String tele = txtTelefono.getText().toString();
                String e =  txtCelular.getText().toString();
                String con =  txtContacto.getText().toString();
                String ruc =  txtRUC.getText().toString();



                if (!raz.matches(ValidacionUtil.NOMBRE)){
                    mensajeAlert("Razon es de 3 a 30 ");
                } else if (!ruc.matches(ValidacionUtil.RUC)){
                    mensajeAlert("Ruc es de 11 ");
                } else if (!dir.matches(ValidacionUtil.DIRECCION)){
                    mensajeAlert("Dirección es de 3 a 30");
                } else if (!tele.matches(ValidacionUtil.TELE)){
                    mensajeAlert("Telefono son 9 digitos");
                } else if (!e.matches(ValidacionUtil.TELE)){
                    mensajeAlert("Celular son 9 digitos");
                } else if (!con.matches(ValidacionUtil.NOMBRE)){
                    mensajeAlert("COntacto es de 3 a 30");
                }else {

                    String pasi= spnPais.getSelectedItem().toString();
                    String idp= pasi.split(":")[0];

                    Pais nip= new Pais();
                    nip.setIdPais(Integer.parseInt(idp));

                    Proveedor obj = new Proveedor();
                    obj.setRazonsocial(raz);
                    obj.setRuc(ruc);
                    obj.setDireccion(dir);
                    obj.setTelefono(tele);
                    obj.setCelular(e);
                    obj.setContacto(con);
                    obj.setEstado(1);
                    obj.setFechaRegistro(FunctionUtil.getFechaActualStringDateTime());
                    obj.setPais(nip);

                    registraPro(obj);


                }


            }
        });



    }
    public void registraPro(Proveedor objet){
        Call<Proveedor> call =  serproo.insertaProveedor(objet);
        call.enqueue(new Callback<Proveedor>() {
            @Override
            public void onResponse(Call<Proveedor> call, Response<Proveedor> response) {
                if (response.isSuccessful()){
                    Proveedor objSalida = response.body();
                    mensajeAlert("Registro Exitoso " + objSalida.getIdProveedor());
                }else{
                    mensajeAlert("Error insret==> ");
                }
            }

            @Override
            public void onFailure(Call<Proveedor> call, Throwable t) {
                mensajeAlert("Error insret==> " + t.getMessage());

            }
        });

    }

    public void listarpaises(){
        Call<List<Pais>> call= serpais.listaTodos();
        call.enqueue(new Callback<List<Pais>>() {
            @Override
            public void onResponse(Call<List<Pais>> call, Response<List<Pais>> response) {
                lstPais = response.body();

                for(Pais obj:lstPais){
                    lstpaises.add( obj.getIdPais() + ": " + obj.getNombre());
                }
                adaptador.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Pais>> call, Throwable t) {

            }
        });
    }



    //mensaje de alerta
    public void mensajeAlert(String msg){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(true);
        alertDialog.show();
    }





}