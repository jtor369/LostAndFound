<?php
require_once 'grabJsonItem.php';
require_once 'ItemDBHelper.php';

$result = false;

$item = grabJsonItem();
if ($item != null)
{
	$refid = ItemDBHelper::putItem($item);
	if ($refid != NULL)
	{
		$result = true;
	}
echo '{"result":"' . ($result ? "true" : "false") . '", "refid":' . $refid . '}';
}else
{
echo '{"result":"' . ($result ? "true" : "false") . '"}';
}


?>