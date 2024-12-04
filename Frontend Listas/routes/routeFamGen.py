from flask import Blueprint, render_template, request, redirect, url_for, flash
import requests
import json

familia_generador = Blueprint('familia_generador', __name__)
URL = 'http://localhost:8090/api/familia_generador'

#####################################################################################
@familia_generador.route('/')
def home():
    try:
        r = requests.get(f'{URL}/list')
        r.raise_for_status()  
        data = r.json()
        return render_template('familiaGenerador/lista.html', lista=data["data"])
    except requests.RequestException as e:
        print(f"Error al obtener la lista de generadores: {e}")
        flash("No se pudo cargar la lista de generadores.", "error")
        return render_template('familiaGenerador/lista.html', lista=[], error="No se pudo cargar la lista de generadores.")
#####################################################################################
@familia_generador.route('/familia_generador/register', methods=['GET'])
def registrar_asociacion():
    try:
        r_generadores = requests.get('http://localhost:8090/api/generador/list')
        r_familias = requests.get('http://localhost:8090/api/familia/list')
        r_generadores.raise_for_status()
        r_familias.raise_for_status()

        generadores = r_generadores.json()["data"]
        familias = r_familias.json()["data"]

        return render_template('familiaGenerador/registro.html', generadores=generadores, familias=familias)
    except requests.RequestException as e:
        flash("Error al cargar los generadores y familias.", "error")
        return redirect(url_for('familia_generador.home'))
#####################################################################################
@familia_generador.route('/familia_generador/save', methods=["POST"])
def guardar_asociacion():
    headers = {'Content-Type': 'application/json'}
    form = request.form
    try:
        data = {
            "generador": int(form['generador']),  
            "familia": int(form['familia']),  
            "caracteristicas": form['descripcion']  
        }
        
        r = requests.post(f'{URL}/save', data=json.dumps(data), headers=headers)
        r.raise_for_status()  
        flash("Asociacion registrada exitosamente", "success")
        return redirect(url_for('familia_generador.home'))
    except requests.RequestException as e:
        if hasattr(e.response, 'text'):
            print("Detalle del error del API:", e.response.text)
        print("Error al registrar la asociacion:", e)
        flash("Error al registrar la asociacion. Intente nuevamente.", "error")
        return redirect(url_for('familia_generador.registrar'))
#####################################################################################
@familia_generador.route('/familia_generador/editar/<int:id>', methods=['GET'])
def editar_asociacion(id):
    try:
        r_generadores = requests.get('http://localhost:8090/api/generador/list')
        r_familias = requests.get('http://localhost:8090/api/familia/list')
        r_generadores.raise_for_status()
        r_familias.raise_for_status()

        generadores = r_generadores.json()["data"]
        familias = r_familias.json()["data"]

        r_asociacion = requests.get(f'http://localhost:8090/api/familia_generador/get/{id}')
        r_asociacion.raise_for_status()
        asociacion = r_asociacion.json()["data"]

        return render_template(
            'familiaGenerador/editar.html',
            generadores=generadores,
            familias=familias,
            familia_generador=asociacion
        )
    except requests.RequestException as e:
        print("Error al cargar la informacion de la asociacion:", e)
        flash("Error al cargar la informacion de la asociacion.", "error")
        return redirect(url_for('familia_generador.home'))
#####################################################################################    
@familia_generador.route('/familia_generador/update', methods=['POST'])
def actualizar_asociacion():
    headers = {'Content-Type': 'application/json'}
    form = request.form
    try:
        data = {
            "id": int(form["id"]),
            "generador": int(form['generador']),
            "familia": int(form['familia']),
            "caracteristicas": form['descripcion']
        }

        r = requests.post('http://localhost:8090/api/familia_generador/update', 
                          data=json.dumps(data), headers=headers)
        r.raise_for_status()  

        flash("Asociacion actualizada exitosamente", "success")
        return redirect(url_for('familia_generador.home'))

    except requests.RequestException as e:
        print("Error al actualizar la asociacion:", e)
        flash("Error al actualizar la asociacion. Intente nuevamente.", "error")
        return redirect(url_for('familia_generador.home'))

#####################################################################################    
@familia_generador.route('/familia_generador/<int:id>/eliminar', methods=['GET'])
def eliminar_asociacion(id):
    try:
        r = requests.delete(f'{URL}/delete/{id}')
        r.raise_for_status()
        flash("Asociacion eliminada exitosamente", "success")
        return redirect(url_for('familia_generador.home'))
    except requests.RequestException as e:
        flash("Error al eliminar la asociacion. Intente nuevamente.", "error")
        return redirect(url_for('familia_generador.home'))