<?php 

include 'conexion.php';

$idPelicula =$_POST['idPelicula'];
$foto=$_POST['foto'];


$query ="UPDATE peliculas_fotos SET 
foto ='$foto' WHERE idPelicula LIKE '$idPelicula'";

$resultado =mysqli_query($conexion,$query);

if($resultado){
    echo "Datos actualizados";
}else{
    echo "Error no se pudo actualizar";
}


mysqli_close($conexion);

?>