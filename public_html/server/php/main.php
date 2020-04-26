<?php 
$conn = mysqli_connect('10.178.1.63', 'Team201810' , 'ABCabc123' );
if(!$conn)
{
    die('Faled to connect: ' . mysqli_error($conn));
}
mysqli_select_db($conn,"Team201810");

class Dataset
{
    public $vnum;
    public $pnum;
    public $points;
}

//调用java接口的准备函数
function http_post_advertise($url,$data){
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_POST, true);

    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, FALSE);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);

    curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
    curl_setopt($ch, CURLOPT_HTTPHEADER,array());
    $result = curl_exec($ch);
    curl_close($ch);
    return $result;
}

if(isset($_POST["points"]))
{
    $d = $_POST["points"];          //接收前端的数据
    $points = json_decode($d, true);

    $sql = "SELECT amt FROM current";
    $result = mysqli_query($conn, $sql);

    $current = array();
    $i = 0;
    if (mysqli_num_rows($result) > 0) 
    {
        while($row = mysqli_fetch_assoc($result)) 
        {
            $current[$i]= $row["amt"];
            $i++;
        }

        $start = array();
        $start["lat"] = $current[0];
        $start["lng"] = $current[1];

        if (isset($_POST["numOfVehicle"]))
        {
            //整合数据发送给java接口
            $vNum = $_POST["numOfVehicle"];
            $data = array_merge($start, $points);
            $pNum = count($data);
            $dataset = new Dataset();
            $dataset->vnum = $vNum;
            $dataset->pnum = $pNum;
            $dataset->points = $data;
            json_encode($dataset);

            //调用java接口
            $url= "http://10.178.1.63/~Team201810/server/algorithms/Main.java";
            $http_article=http_post_advertise($url,$dataset); //这里要输入java接口的url
            $arr_article= json_decode($http_article,true);
            $res_article=$arr_article['result'];

            echo json_encode($res_article);               //发送结果给web前端

            $result = array();
            for($i = 0; i < $pNum -1; $i++)
            {
                $result[i] = $points[$result[i]];
                $sql = "INSERT INTO routes (lat, lng)
                VALUES ($result[i]['lat'],$result[i]['lng'])";
                mysqli_query($conn,$sql);
            }
        }   
    }
}
mysql_close($conn);
?>