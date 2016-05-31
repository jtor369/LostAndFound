<?php

class Location
{
	var $refid;
	var $lat;
	var $lon;
	var $radius;
	
	function __construct($refid, $lat, $lon, $radius)
	{
		$this->refid = intval($refid);
		$this->lat = doubleval($lat);
		$this->lon = doubleval($lon);
		$this->radius = doubleval($radius);
	}
}

?>