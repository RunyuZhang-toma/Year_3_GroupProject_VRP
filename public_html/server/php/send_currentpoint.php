<?php 
$conn = mysqli_connect('10.178.1.63', 'Team201810' , 'ABCabc123' );
if(!$conn)
{
    die('Faled to connect: ' . mysqli_error($conn));
}
mysqli_select_db($conn,"Team201810");

$sql = "SELECT amt FROM current";
$result = mysqli_query($conn, $sql);

$current = array();
$i = 0;
if (mysqli_num_rows($result) > 0) 
{
    // 输出数据
    while($row = mysqli_fetch_assoc($result)) 
    {
        $current[$i]= $row["amt"];
        $i++;
    }
}

echo json_encode($current);
mysql_close($conn);
?>