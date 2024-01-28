<?php
$conexion =mysqli_connect('localhost','apiuser','apiuser','peliculas');
if(!$conexion){
    echo "Error al conectar";
}
