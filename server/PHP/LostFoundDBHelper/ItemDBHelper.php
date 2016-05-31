<?php
require_once 'DBInfo.php';
require_once 'Item.php';


class ItemDBHelper
{
	static function putLocationQuery(Location $location)
	{
		$query = "INSERT INTO location (refid, lat, lon, radius) VALUES (" . $location->refid . ", " . $location->lat . ", " . $location->lon . ", " . $location->radius . ");";

		return $query;
	}
	
	static function putTagsQueries($taglist)
	{
		$queries = array();
		
		foreach ($taglist as $tag)
		{
			$query = "INSERT INTO tags (refid, tag) VALUES (" . $tag->refid . ", '" . $tag->tag . "');";
			array_push($queries, $query);
		}
		
		return $queries;
	}
	
	static function putItem(Item $item)
	{
		$connection = ItemDBHelper::getDBConnection();
		$success = true;
		
		if ($connection != null)
		{
			$itemMain = $item->getItemMain(-1);
			$query = "INSERT INTO items (userid, timestamp, description, thumbnail) VALUES (" . $itemMain->userid . "," . $itemMain->timestamp . ",'" . $itemMain->description . "','" . $itemMain->thumbnail . "');";

			if ($bob = $connection->query($query) == true)
			{
				$refid = $connection->insert_id;
				$itemtags = $item->getTags($refid);
				$location = $item->getLocation($refid);
				
				$locationQuery = ItemDBHelper::putLocationQuery($location);
				$tagsQueries = ItemDBHelper::putTagsQueries($itemtags);
				
				$res = $connection->query($locationQuery);
				
				$success &= $res;
				
				foreach ($tagsQueries as $tagQuery)
				{
					$res = $connection->query($tagQuery);
					$success &= $res;
				}
			}else
			{
				$success = false;
			}
			$connection->close();
		}else
		{
			$success = false;
		}
		
		
		return $success ? $refid: NULL;
		
	}
	
	static function getItemMain(mysqli $connection, $refid)
	{
		$query = "SELECT * FROM items WHERE refid = " . $refid . ";";
		$result = $connection->query($query);
		if ($result != null)
		{
			 $temp = $result->fetch_assoc();
			 $itemmain = new ItemMain($temp['refid'], $temp['description'],$temp['userid'], $temp['timestamp'], $temp['thumbnail']);
			 return $itemmain;
		}
		return null;
	}
	
	static function getLocation($connection,$refid)
	{
		$query = "SELECT * FROM location WHERE refid = " . $refid . ";";
		$result = $connection->query($query);
		if ($result != null)
		{
			$temp = $result->fetch_assoc();
			$location = new Location($temp['refid'], $temp['lat'], $temp['lon'], $temp['radius']);
			return $location;
		}
		return null;
	}
	
	static function getTagList($connection, $refid)
	{
		$query = "SELECT * FROM tags WHERE refid = " . $refid . ";";
		$result = $connection->query($query);
		$taglist = array();
		if ($result != null)
		{
			
			
			while ($temp = $result->fetch_assoc())
			{
				$tag = new Tag($temp['refid'],$temp['tag']);
				array_push($taglist, $tag);
			}
			
			//return $taglist;
		}
		return $taglist;
	}
	
	static function getItem($refid)
	{
// 		echo "getting " . $refid;
		$connection = ItemDBHelper::getDBConnection();
		$item = null;
		
		if ($connection != null)
		{
			 $itemmain = ItemDBHelper::getItemMain($connection, $refid);
			 $location = ItemDBHelper::getLocation($connection, $refid);
			 $taglist = ItemDBHelper::getTaglist($connection, $refid);
			 
// 			 var_dump($itemmain);
// 			 var_dump($location);
// 			 var_dump($taglist);
			 
			 if ($itemmain != null && $location != null)
			 {
			 	$item = Item::combine($itemmain, $taglist, $location);
// 			 	var_dump($item);
			 }
			 $connection->close();
		}else
		{
			echo "couldn't connect";
			
		}
		
		return $item;
		
	}
	
	static function searchForItems(Item $item)
	{
		$items = array();
		
			
			$location = $item->getLocation($item->refid);
			$lat = $location->lat;
			$lon = $location->lon;
			$radius = $location->radius; //$radius must be in km
			
			$r_earth = doubleval(6378000);
			$pi = 3.14159265358979;
			
			$lat_min = $lat - ($radius / $r_earth) * (180 / $pi);
			$lat_max = $lat + ($radius / $r_earth) * (180 / $pi);
			$lon_min = $lon - ($radius / $r_earth) * (180 / $pi) / cos($lat * $pi/180);
			$lon_max = $lon + ($radius / $r_earth) * (180 / $pi) / cos($lat * $pi/180);
			//new_latitude  = latitude  + (dy / r_earth) * (180 / pi);
			//new_longitude = longitude + (dx / r_earth) * (180 / pi) / cos(latitude * pi/180);
			$locationquery = "SELECT refid FROM location WHERE lat >= " . $lat_min . " and lat <= " . $lat_max . " and lon >= " . $lon_min . " and lon <= " . $lon_max . ";";
			
			
			$conn = ItemDBHelper::getDBConnection();
			if ($conn != null)
			{
				$res = $conn->query($locationquery);
				
				
				while($result = $res->fetch_assoc())
				{
					$refid = $result['refid'];
					//echo $refid;
					array_push($items, ItemDBHelper::getItem($refid));
				}
				//var_dump($items);
					
				$conn->close();
			}
			//echo $locationquery;
		
		
		return $items;
		
	}
	
	static function getDBConnection()
	{
		$conn = new mysqli(DBInfo::$servername, DBInfo::$username, DBInfo::$password, DBInfo::$database);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
			return NULL;
		}
		return $conn;
	}
	
}
?>