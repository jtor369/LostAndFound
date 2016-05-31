<?php

class Tag
{	
	var $refid;
	var $tag;
	
	function __construct($refid, $tag)
	{
		$this->refid = intval($refid);
		$this->tag = $tag;
	}
}

?>