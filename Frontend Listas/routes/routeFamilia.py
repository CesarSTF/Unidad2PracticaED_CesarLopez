from flask import Blueprint, render_template, request, redirect, url_for, flash
import requests
import json

familia = Blueprint('familia', __name__)
URL = 'http://localhost:8090/api/familia'

#####################################################################################
@familia.route('/familia')
def home_familia():
    criterio = request.args.get('criterio') 
    tipo = request.args.get('tipo')  
    metodo = request.args.get('metodo')  

    if criterio and tipo and metodo:
        return redirect(url_for('familia.ordenar_familias', attribute=criterio, type=tipo, metodo=metodo))

    try:
        r = requests.get(f'{URL}/list')
        r.raise_for_status()
        data = r.json()

        return render_template('familia/lista.html', lista=data["data"], 
                               criterio=criterio, tipo=tipo, metodo=metodo)
    except requests.RequestException as e:
        flash("Error al cargar la lista de familias.", "error")
        return render_template('familia/lista.html', lista=[])
#####################################################################################
@familia.route('/familia/editar/<int:id>', methods=['GET'])
def editar_familia(id):
    try:
        r = requests.get(f"{URL}/get/{id}")
        r.raise_for_status()
        data = r.json()
        print(data)
        return render_template('familia/editar.html', familia=data["data"])
    except requests.RequestException as e:
        print(f"Error al obtener la familia con ID {id}: {e}")
        flash("Error al cargar los datos de la familia.", "error")
        return redirect(url_for('familia.home_familia'))
#####################################################################################
@familia.route('/familia/register', methods=['GET'])
def registrar():
    return render_template('familia/registro.html')
#####################################################################################
@familia.route('/familia/save', methods=["POST"])
def guardar_familia():
    headers = {'Content-Type': 'application/json'}
    form = request.form
    try:
        dataF = {
            "NombreFamilia": form['nombreFamilia'],
            "NroIntegrantes": int(form['nroIntegrantes']),
            "saldo":float(form['saldo'])
        }
        r = requests.post(f'{URL}/save', data=json.dumps(dataF), headers=headers)
        dat = r.json()
        print(dat)
        flash("Familia registrada exitosamente", "success")
        return redirect(url_for('familia.home_familia'))
    except requests.RequestException as e:
        print(f"Error al registrar la familia: {e}")
        flash("Error al registrar la familia. Intente nuevamente.", "error")
        return redirect(url_for('familia.registrar'))
#####################################################################################
@familia.route('/familia/update', methods=['POST'])
def actualizar_familia():
    headers = {'Content-Type': 'application/json'}
    form = request.form
    try:
        dataF = {
            "id": form["id"],
            "NombreFamilia": form['nombreFamilia'],
            "NroIntegrantes": int(form['nroIntegrantes']),
            "saldo": float(form['saldo'])
        }
        r = requests.post(f'{URL}/update', data=json.dumps(dataF), headers=headers)
        dat = r.json()
        print(dat)
        flash("Familia actualizada exitosamente", "success")
        return redirect(url_for('familia.home_familia'))
    except requests.RequestException as e:
        print(f"Error al actualizar la familia: {e}")
        flash("Error al actualizar la familia. Intente nuevamente.", "error")
        return redirect(url_for('familia.home_familia'))
#####################################################################################
@familia.route('/familia/<int:id>/eliminar', methods=['GET'])
def eliminar_familia(id):
    try:
        r = requests.delete(f'{URL}/delete/{id}')
        r.raise_for_status()
        flash("Familia eliminada exitosamente", "success")
        return redirect(url_for('familia.home_familia'))
    except requests.RequestException as e:
        print(f"Error al eliminar la familia con ID {id}: {e}")
        flash("Error al eliminar la familia. Intente nuevamente.", "error")
        return redirect(url_for('familia.home_familia'))
#####################################################################################
@familia.route('/familia/ordenar/<attribute>/<int:type>/<metodo>', methods=['GET'])
def ordenar_familias(attribute, type, metodo):
    try:
        url = f'{URL}/list/order/{attribute}/{type}/{metodo}'
        r = requests.get(url)
        r.raise_for_status()  
        data = r.json()

        if r.status_code == 200:
            if not data.get("data"):
                flash("No se encontraron resultados para los criterios especificados.", "warning")
            
            # Pasar los valores actuales a la plantilla
            return render_template('familia/lista.html', lista=data.get("data", []),
                                   criterio=attribute, tipo=type, metodo=metodo)
        else:
            flash(f"Error al ordenar las familias: {data.get('msg', 'Error desconocido')}", "error")
            return render_template('familia/lista.html', lista=[])

    except requests.RequestException as e:
        print(f"Error al ordenar familias: {e}")
        flash("Error al ordenar las familias. Intente nuevamente.", "error")
        return render_template('familia/lista.html', lista=[])
#######################################################################################3
@familia.route('/familia/search/<criterio>/<texto>', methods=['GET'])
def buscar_familia(criterio, texto):
    url = "http://localhost:8090/api/familia/search/"
    lista = []
    
    try:
        if criterio in ['nombrefamilia','nrointegrantes', 'saldo', 'codigoFamilia']:
            r = requests.get(url + criterio + '/' + texto)
        else:
            flash("Criterio de búsqueda no válido.", category='error')
            return redirect(url_for('familia.home_familia'))

        if r.status_code == 200:
            if r.text.strip():  
                data1 = r.json()
                # Convertir la lista enlazada en una lista normal
                if "data" in data1 and "head" in data1["data"]:
                    current = data1["data"]["head"]
                    while current:
                        lista.append(current["data"])
                        current = current.get("next", None)
                return render_template('familia/lista.html', lista=lista)
            else:
                flash("La respuesta del servidor esta vacia.", category='error')
        else:
            flash(f"Error al obtener datos: {r.status_code}", category='error')
    
    except requests.exceptions.RequestException as e:
        flash(f"Error de conexion: {str(e)}", category='error')
    except ValueError as e:
        flash("Error al procesar la respuesta del servidor.", category='error')

    return redirect(url_for('familia.home_familia'))
