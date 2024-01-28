<?php 
include 'conexion.php';

$idPelicula =$_POST['idPelicula'];
$result= array();
$result['peliculas_fotos'] =array();
$query ="SELECT * FROM peliculas_fotos WHERE idPelicula LIKE '$idPelicula' ";
$response = mysqli_query($conexion,$query);
while($row = mysqli_fetch_array($response))
{
    $index['idPelicula'] =$row['0'];
    $index['foto'] =$row['1'];
    array_push($result['peliculas_fotos'], $index);
}
$result["exito"]="1";
echo json_encode($result);

?>