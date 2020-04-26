<?php
$conn = mysqli_connect('10.178.1.63', 'Team201810' , 'ABCabc123' );
if(!$conn)
{
    die('Faled to connect: ' . mysqli_error($conn));
}
mysqli_select_db($conn,"Team201810");

echo 'Receive the command successfully';
if (isset($_POST["submitCmd"]) ) {
    $submit = $_POST["submitCmd"];
    $info = json_decode($submit, true);
    if($info['submitCmd'] == 1 || $info['submitCmd'] == 4){
        echo'111';
        $inf = $info['submitCmd'];
        $sql="UPDATE config SET v = $inf WHERE flightdata = 'instruction'";
        mysqli_query($conn,$sql);
    }
    else if($info['submitCmd'] >= 11 && $info['submitCmd'] <= 13){
        $inf = $info['submitCmd'];
        $sql="UPDATE config SET v = $inf WHERE flightdata = 'speed'";
        mysqli_query($conn,$sql);
    }
    else if($info['submitCmd'] >= 21 && $info['submitCmd'] <= 23){
        $inf = $info['submitCmd'];
        $sql="UPDATE config SET v = $inf WHERE flightdata = 'altitude'";
        mysqli_query($conn,$sql);
    }
    else if($info['submitCmd'] >= 30 && $info['submitCmd'] <= 33){
        $inf = $info['submitCmd'];
        $sql="UPDATE config SET v = $inf WHERE flightdata = 'FinishAction'";
        mysqli_query($conn,$sql);
    }
    else if($info['submitCmd'] >= 40 && $info['submitCmd'] <= 43){
        $inf = $info['submitCmd'];
        $sql="UPDATE config SET v = $inf WHERE flightdata = 'HeadingMode'";
        mysqli_query($conn,$sql);
    }
    echo 'Receive the command successfully';
}
?>