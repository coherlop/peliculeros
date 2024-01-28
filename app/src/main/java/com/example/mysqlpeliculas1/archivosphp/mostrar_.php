<?php 
include 'conexion.php';


$result= array();
$result['peliculas'] =array();
$query ="SELECT *FROM peliculas";
$responce = mysqli_query($conexion,$query);

while($row = mysqli_fetch_array($responce))
{
    $index['idPelicula'] =$row['0'];
    $index['titulo'] =$row['1'];
    $index['genero'] =$row['2'];
    

    array_push($result['peliculas'], $index);

}
$result["exito"]="1";
echo json_encode($result);

?>