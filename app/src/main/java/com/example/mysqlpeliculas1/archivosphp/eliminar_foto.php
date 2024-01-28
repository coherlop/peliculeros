<?php 

include 'conexion.php';

$idPelicula = $_POST["idPelicula"];
$query = "DELETE FROM peliculas_fotos WHERE idPelicula LIKE '$idPelicula'";
$result=mysqli_query($conexion,$query);
if($result){
    echo "Foto eliminada";
}else{
    echo "No se pudo eliminar la foto";
}

mysqli_close($conexion);

?>