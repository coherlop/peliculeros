<?php 

include 'conexion.php';

$idPelicula = $_POST['idPelicula'];
$titulo = $_POST['titulo'];
$genero = $_POST['genero'];


$query ="UPDATE peliculas SET 
idPelicula = '$idPelicula', titulo = '$titulo' , genero = '$genero' 
WHERE idPelicula LIKE '$idPelicula'";

$resultado = mysqli_query($conexion,$query);

if($resultado){
    echo "datos actualizados";
}else{
    echo "no se pudo actualizar";
}


mysqli_close($conexion);

?>