<?php

include("config.php");



$sql = "SELECT * FROM penduduk";
$result = array();
$query = mysqli_query($db, $sql);
 
while($row = mysqli_fetch_array($query)){
    array_push($result, array('nama_penduduk' => $row['nama_penduduk'],
    'ttl_penduduk' => $row['ttl_penduduk'],
    'hp_penduduk' => $row['hp_penduduk'],
    'alamat_penduduk' => $row['alamat_penduduk']
    
));
}
echo json_encode(array("result" => $result));
?>



