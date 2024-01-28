<?php 

include 'conexion.php';
$idPelicula =$_POST['idPelicula'];
$foto =$_POST['foto'];


// aqui escribimos codigo sql
$query ="INSERT INTO peliculas_fotos(idPelicula,foto) 
values('$idPelicula' ,'$foto') ";
$resultado =mysqli_query($conexion,$query);

if($resultado){
    echo "Foto insertada";
}else{
    echo "No se pudo insertar la foto";
}
mysqli_close($conexion);

?>