<?php 

include 'conexion.php';
$idPelicula =$_POST['idPelicula'];
$titulo =$_POST['titulo'];
$genero =$_POST['genero'];


// aqui escribimos codigo sql
$query ="INSERT INTO peliculas(idPelicula,titulo,genero) 
values('$idPelicula' ,'$titulo', '$genero') ";
$resultado =mysqli_query($conexion,$query);

if($resultado){
    echo "Datos insertados";
}else{
    echo "No se pudo insertar la película";
}
mysqli_close($conexion);

?>