<?php 

include 'conexion.php';

$idPelicula = $_POST["idPelicula"];
$query = "DELETE FROM peliculas WHERE idPelicula LIKE '$idPelicula'";
$result=mysqli_query($conexion,$query);
if($result){
    echo "Datos eliminados";
}else{
    echo "No se pudo eliminar";
}

mysqli_close($conexion);

?>