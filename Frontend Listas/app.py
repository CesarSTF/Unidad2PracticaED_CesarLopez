from flask import Flask

def create_app():
    app = Flask(__name__, instance_relative_config=False)
    with app.app_context():
        from routes.routeGenerador import generador  
        app.register_blueprint(generador)
        from routes.routeFamilia import familia
        app.register_blueprint(familia)
        from routes.routeFamGen import familia_generador  
        app.register_blueprint(familia_generador) 
    return app
