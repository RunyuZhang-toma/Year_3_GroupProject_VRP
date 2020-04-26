<?php  
$conn = mysqli_connect('10.178.1.63', 'Team201810' , 'ABCabc123' );
if(!$conn)
{
    die('Faled to connect: ' . mysqli_error($conn));
}
mysqli_select_db($conn,"Team201810");

if($_POST["ready"]==1)
{
    class Result
    {
        public $Infor;
        public $Config=array();
        public $Positions=array();
    }
    //get the instruction from the database
    $sql = "SELECT v FROM config WHERE flightdata = 'instruction'";
    $instr = mysqli_query($conn, $sql);
    $row = mysqli_fetch_assoc($instr);
    $instruction = $row["v"];
    if($instruction == -1 || $instruction == 4)
    {
        $result = new Result();
        $result->Infor = $instruction;
        echo json_encode($result,JSON_FORCE_OBJECT);
    }
    //If the takeoff instruction is 1, send the flight data to the app
    else if($instruction == 1)
    {
        $sql = "SELECT lat , lng FROM routes";
        $routes = mysqli_query($conn, $sql);

        $positions=array();
        $i = 0;
        if (mysqli_num_rows($routes) > 0) 
        {   
            // 输出数据
            while($row = mysqli_fetch_assoc($routes)) 
            {
                $position[$i]['Lat']= $row['lat'];
                $position[$i]['Lng']= $row['lng'];
                $i++;
            }   
        }
        else
        {
            echo "no routes";
        }

        $sql = "SELECT v FROM config";
        $information = mysqli_query($conn, $sql);

        $conf =array();
        $j=0;
        if (mysqli_num_rows($information) > 0) 
        {
            $row = mysqli_fetch_assoc($information);
            $infor = $row['v'];
            while($row = mysqli_fetch_assoc($information)) 
            {
                $conf[$j] = $row['v'];
                $j++;
            }
        }
        else
        {
            echo "no config";
        }

        //Prepare the information pack and send it to the app
        $config =array();
        $config['Altitude']=$conf[0];
        $config['Speed']=$conf[1];
        $config['FinishAction']=$conf[2];
        $config['HeadingMode']=$conf[3];
        $result = new Result();
        $result->Infor = $infor;
        $result->Config = $config;
        $result->Positions = $position;
        echo json_encode($result);                   //发送结果给app

        //Set the config value back to the default values
        $sql1="UPDATE config SET v = 11 WHERE flightdata = 'speed'";
        $sql2="UPDATE config SET v = 21 WHERE flightdata = 'altitude'";
        $sql3="UPDATE config SET v = 31 WHERE flightdata = 'finishaction'";
        $sql4="UPDATE config SET v = 41 WHERE flightdata = 'headingmode'";
        $sql5="UPDATE config SET v = -1 WHERE flightdata = 'instruction'";

        mysqli_query($conn,$sql1);
        mysqli_query($conn,$sql2);
        mysqli_query($conn,$sql3);
        mysqli_query($conn,$sql4);
        mysqli_query($conn,$sql5);

        //Delete the routes information from the database
        mysqli_query($conn,"DELETE FROM config");
    }
}
mysql_close($conn);
?>
    