<?php
class ItemMain
{
    var $refid;
    var $description;
    var $userid;
    var $timestamp;
	
    var $thumbnail; //base64

	
	function __construct($refid, $description,$userid, $timestamp, $thumbnail)
	{
		$this->refid = $refid;
		$this->description = $description;
		$this->userid = $userid;
		$this->timestamp = $timestamp;
		$this->thumbnail = $thumbnail;
	}
}
?>