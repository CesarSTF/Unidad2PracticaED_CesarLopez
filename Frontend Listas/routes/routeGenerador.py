from flask import Blueprint, render_template, request, redirect, url_for, flash, abort
import requests
import json

generador = Blueprint('generador', __name__)
URL = 'http://localhost:8090/api/generador'
#####################################################################################
@generador.route('/generador')
def home_generador():
    criterio = request.args.get('criterio') 
    tipo = request.args.get('tipo')  
    metodo = request.args.get('metodo')  

    if criterio and tipo and metodo:
        return redirect(url_for('generador.ordenar_generador', attribute=criterio, type=tipo, metodo=metodo))

    try:
        r = requests.get(f'{URL}/list')
        r.raise_for_status()  
        data = r.json()      
        return render_template('generador/lista.html', lista=data["data"],
                               criterio=criterio, tipo=tipo, metodo=metodo)
    except requests.RequestException as e:
        print(f"Error al obtener la lista de generadores: {e}")
        flash("No se pudo cargar la lista de generadores.", "error")
        return render_template('generador/lista.html', lista=[], error="No se pudo cargar la lista de generadores.")
#####################################################################################
@generador.route('/generador/register')
def registrar():
    try:
        r = requests.get(f"{URL}/listType")
        r.raise_for_status()  
        data = r.json()        
        return render_template('generador/registro.html', lista=data["data"])
    except requests.RequestException as e:
        print(f"Error al obtener los tipos de generadores: {e}")
        flash("No se pudieron cargar los tipos de generadores.", "error")
        return redirect(url_for('generador.home_generador'))
#####################################################################################
@generador.route('/generador/editar/<int:id>', methods=['GET'])
def editar_generador(id):
        r = requests.get(f"{URL}/listType")
        data = r.json()
        r1 = requests.get(f"{URL}/get/{id}")
        data1 = r1.json()
        if(r1.status_code == 200):
            return render_template('generador/editar.html', lista=data["data"], generador = data1["data"])
        else:
            flash(data1["data"], category='error')
            return redirect(url_for('generador.home_generador'))
#####################################################################################
@generador.route('/generador/save', methods=["POST"])
def guardar_generador():
    headers = {'Content-type': 'application/json'}
    form = request.form
    try:
        dataF = {
            "modelo": form['modelo'],
            "consumo": float(request.form.get('consumoComustible')),
            "costo": float(request.form.get('costo')),
            "energia": float(request.form.get('enegeriaGenerada')),
            "uso": request.form.get('uso')
        }
        print(dataF)
        r = requests.post(f'{URL}/save', data=json.dumps(dataF), headers=headers)
        dat = r.json()
        print(dat)
        flash("Generador registrado exitosamente", "success")
        return redirect(url_for('generador.home_generador'))
    except requests.RequestException as e:
        print(f"Error al registrar el generador: {e}")
        flash("Error al registrar el generador. Intente nuevamente.", "error")
        return redirect(url_for('generador.registrar'))
#####################################################################################
@generador.route('/generador/update', methods=['POST'])
def actualizar_generador():
    headers = {'Content-Type': 'application/json'}
    form = request.form
    
    dataF = {
            "id": form["id"],
            "modelo": request.form.get('modelo'),
            "consumo": float(request.form.get('consumoComustible')),
            "costo": float(request.form.get('costo')),
            "energia": float(request.form.get('enegeriaGenerada')),
            "uso": request.form.get('uso')
        }
    print(dataF)
    r = requests.post(f'{URL}/update', data=json.dumps(dataF), headers=headers)
    dat = r.json()
    print(dat)
    flash("Generador actualizado exitosamente", "success")
    return redirect(url_for('generador.home_generador'))
#####################################################################################    
@generador.route('/generador/<int:id>/eliminar', methods=['GET'])
def eliminar_generador(id):
    try:
        r = requests.delete(f'{URL}/delete/{id}')
        r.raise_for_status()
        flash("Generador eliminado exitosamente", "success")
        return redirect(url_for('generador.home_generador'))
    except requests.RequestException as e:
        print(f"Error al eliminar el generador con ID {id}: {e}")
        flash("Error al eliminar el generador. Intente nuevamente.", "error")
        return redirect(url_for('generador.home_generador'))
#####################################################################################
def cargar_opciones(endpoint):
    try:
        r = requests.get(endpoint)
        r.raise_for_status()
        data = r.json()
        return [(item, item) for item in data.get("data", [])]
    except requests.RequestException as e:
        print(f"Error al cargar opciones desde {endpoint}: {e}")
        return []
#####################################################################################
@generador.route('/generador/ordenar/<attribute>/<int:type>/<metodo>', methods=['GET'])
def ordenar_generador(attribute, type, metodo):
    try:
        url = f'{URL}/list/order/{attribute}/{type}/{metodo}'
        r = requests.get(url)
        r.raise_for_status()  
        data = r.json()

        if r.status_code == 200:
            if not data.get("data"):
                flash("No se encontraron resultados para los criterios especificados.", "warning")
            
            # Pasar los valores actuales a la plantilla
            return render_template('generador/lista.html', lista=data.get("data", []),
                                   criterio=attribute, tipo=type, metodo=metodo)
        else:
            flash(f"Error al ordenar los generadores: {data.get('msg', 'Error desconocido')}", "error")
            return render_template('generador/lista.html', lista=[])

    except requests.RequestException as e:
        print(f"Error al ordenar generadores: {e}")
        flash("Error al ordenar los generadores. Intente nuevamente.", "error")
        return render_template('generador/lista.html', lista=[])
#####################################################################################
@generador.route('/generador/search/<criterio>/<texto>', methods=['GET'])
def buscar_generador(criterio, texto):
    url = "http://localhost:8090/api/generador/search/"
    lista = []
    
    try:
        if criterio in ['modelo','costo', 'uso', 'codigoGenerador']:
            r = requests.get(url + criterio + '/' + texto)
        else:
            flash("Criterio de búsqueda no valido.", category='error')
            return redirect(url_for('generador.home_generador'))

        if r.status_code == 200:
            if r.text.strip():  
                data1 = r.json()
                # Convertir lista enlazada en una lista normal
                if "data" in data1 and "head" in data1["data"]:
                    current = data1["data"]["head"]
                    while current:
                        lista.append(current["data"])
                        current = current.get("next", None)
                return render_template('generador/lista.html', lista=lista)
            else:
                flash("La respuesta del servidor esta vacia.", category='error')
        else:
            flash(f"Error al obtener datos: {r.status_code}", category='error')
    
    except requests.exceptions.RequestException as e:
        flash(f"Error de conexion: {str(e)}", category='error')
    except ValueError as e:
        flash("Error al procesar la respuesta del servidor.", category='error')

    return redirect(url_for('generador.home_generador'))
#####################################################################################
