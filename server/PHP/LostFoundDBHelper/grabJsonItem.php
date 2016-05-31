<?php
require_once 'Item.php';
function grabJsonItem()
{
	$item = null;
	if (!empty($_GET))
	{
		if (array_key_exists("jsonItem", $_GET))
		{
			$jsonItem = json_decode($_GET["jsonItem"],true);
			
			if (Item::check_defined($jsonItem))
			{
				$item = Item::getFromArray($jsonItem);
			}
		}
	}
	if (!empty($_POST))
	{
		if (array_key_exists("jsonItem", $_POST))
		{
			$jsonItem = json_decode($_POST["jsonItem"],true);
			
			if (Item::check_defined($jsonItem))
			{
				$item = Item::getFromArray($jsonItem);
			}
		}
	}
	return $item;
}
?>