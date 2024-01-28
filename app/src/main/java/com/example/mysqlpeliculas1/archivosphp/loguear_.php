<?php 
include 'conexion.php';

$email =$_POST['email'];
$password =$_POST['password'];

$result= array();
$result['usuarios'] =array();
$query ="SELECT * FROM usuarios 
WHERE email LIKE '$email' AND password LIKE '$password' "; //esto es mientras la app está en producción, no es el código definitivo para una app terminada, habría que sustituirlo por sentencias preparadas
$response = mysqli_query($conexion,$query);
if($row = mysqli_fetch_array($response))
{
    echo "Autenticacion exitosa";
}

?>