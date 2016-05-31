<?php
require_once 'ItemMain.php';
require_once 'Location.php';
require_once 'Tag.php';


class Item
{
    var $refid;
    var $description;
    var $userid;
    var $timestamp;
	
    var $thumbnail; //base64
    var $tags;		//list<string>
	var $location;	//location (lat, lon, radius)
	
	function __construct($refid, $description,$userid, $timestamp, $thumbnail, $tags, $location)
	{
		$this->refid = intval($refid);
		$this->description = $description;
		$this->userid = intval($userid);
		$this->timestamp = intval($timestamp);
		$this->thumbnail = $thumbnail;
		$this->tags = $tags;
		$this->location = $location;
		
	}
	
	static function check_defined($jsonItem)
	{
		$defined = false;
		if ($jsonItem != null)
		{
			$defined = true;
			$defined &= array_key_exists("refid", $jsonItem);
			$defined &= array_key_exists("description", $jsonItem);
			$defined &= array_key_exists("userid", $jsonItem);
			$defined &= array_key_exists("timestamp", $jsonItem);
			$defined &= array_key_exists("thumbnail", $jsonItem);
			$defined &= array_key_exists("tags", $jsonItem);
			$defined &= array_key_exists("location", $jsonItem);
		}
		
		return $defined;
	}
	
	static function getFromArray($jsonItem)
	{
		if (Item::check_defined($jsonItem))
		{
			$refid = $jsonItem["refid"];
			$description = $jsonItem["description"];
			$userid = $jsonItem["userid"];
			$timestamp = $jsonItem["timestamp"];
			$thumbnail = $jsonItem["thumbnail"];
			$tags = $jsonItem["tags"];
			$location = $jsonItem["location"];
			
			$item = new Item($refid, $description, $userid, $timestamp,$thumbnail, $tags, $location);
			return $item;
		}
	}
	
	
	static function combine($itemmain, $taglist, $location)
	{
		$refid = $itemmain->refid;
		$description = $itemmain->description;
		$userid = $itemmain->userid;
		$timestamp = $itemmain->timestamp;
		$thumbnail = $itemmain->thumbnail;
		
		$tags = array();
		
		foreach ($taglist as $tag)
		{
			array_push($tags, $tag->tag);
		}
		
		
		$location = array("lat"=>$location->lat, "lon"=>$location->lon, "radius"=>$location->radius);
			
		$item = new Item($refid, $description, $userid, $timestamp,$thumbnail, $tags, $location);
		return $item;
	}
	
	function getItemMain($refid){
		return new ItemMain($refid, $this->description, $this->userid, $this->timestamp, $this->thumbnail);
	}
		
	function getTags($refid)
	{
		$tags = array();
		foreach ($this->tags as $tag)
		{
			array_push($tags, new Tag($refid,$tag));
		}
		return $tags;
	}
	
	function getLocation($refid)
	{
		return new Location($refid,$this->location["lat"],$this->location["lon"],$this->location["radius"]);
	}
	
}

?>