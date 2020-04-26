<?php  
$conn = mysqli_connect('10.178.1.63', 'Team201810' , 'ABCabc123' );
if(!$conn)
{
    die('Faled to connect: ' . mysqli_error($conn));
}
mysqli_select_db($conn,"Team201810");

if (isset($_POST["Location"])) 
{
    $d = $_POST["Location"];
    $current = json_decode($d, ture);
    $lat = $current['Lat'];
    $lng = $current['Lng'];
    echo $current['Lat'];
    $sql1="UPDATE current SET amt = $lat WHERE info = 'lat'";
    $sql2="UPDATE current SET amt = $lng WHERE info = 'lng'";
    mysqli_query($conn,$sql1);
    mysqli_query($conn,$sql2);
    mysql_close($conn);
}

mysql_close($conn);
?>