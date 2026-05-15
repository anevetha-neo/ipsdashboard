/*

jQuery Fix for data table outerwidth issue.
version: 0.1.1
 
 */
if (typeof jQuery === 'undefined') {
  throw new Error('jQuery DataTable fix plugin requires jQuery')
}
+function ($) {
  'use strict';
  var version = $.fn.jquery.split(' ')[0].split('.')
  if ((version[0] < 2 && version[1] < 9) || (version[0] == 1 && version[1] == 9 && version[2] < 1)) {
    throw new Error('jQuery DataTable fix plugin requires jQuery version 1.9.1 or higher')
  }
}(jQuery);

+function ($) {
  'use strict';
	
	
	var outerWidthOld  = $.fn.outerWidth  ;
	$.fn.outerWidth = function(options) {
		if(options){
			var ret = outerWidthOld.apply(this, arguments);
		}else{
			var ret = outerWidthOld.apply(this, [false]);
		}
		
		return ret;
	}	 
}(jQuery);
