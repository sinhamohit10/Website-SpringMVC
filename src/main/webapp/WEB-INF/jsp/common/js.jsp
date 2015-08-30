<script>
UT.dashData = {
	pickupLimit: ${pickupLimit},
};
UT.dashUtils = {
	hideAllConatiners: function() {
		$("#page-wrapper").find(".main-container").each(function() {
			$(this).addClass("hidden");
		});
	},
	assign: function() {
		$('.save-changes').click(function() {
			
			$.loader.open();
			var oId = $(".assign-delivery-form").attr('data-order-id');
			var date = $(".assign-delivery-form").attr('data-order-date');
			var time = $(".assign-delivery-form").attr('data-order-time');
			var type = $(".assign-delivery-form").attr('data-type');
			var empId;
			
			$(".assign-delivery-form").find("input").each(function() {
				if($(this).is(':checked')) {
					empId = $(this).attr("data-id");
				}
			});
			
			var action = "pickupAdd";
			
			if(type === 'delivery') {
				action = "deliveryAdd";
			} else if(type === 'deliveryDateTime') {
				action = "deliveryDateTimeAdd";
			} else if(type === 'alteration') {
				action = "alterationData";
			} else if(type === 'pickupDateTime') {
				action = "pickupResch";
			} else if(type === 'tailoringUnit') {
				action = "tailoring";
			}
			
			if(!date || time <0) {
				alert("Please select a date and time.");
				$.loader.close();
			} else {
				var formData = {};
				formData['oId'] = oId;
				formData['empId'] = empId;
				formData['date'] = date;
				formData['time'] = time;
				
				$.ajax({
			       type        : 'POST', 
			        url         : '/deliveryHandler?action=' + action,
			        data        : formData,
			        dataType    : 'json',
			        encode      : true
			    }).done(function(data) {
			      	$.loader.close();
			       	if(data.result) {
			        	alert("Operation Completed.")
			        	if(type != 'pickupDateTime') {
			        		$("#" + data.id).remove();	
			        	}
			       	} else {
			       		alert("Something went wrong.")
			       	}
			    });
				
				$('#myModal').modal('hide');
			}
		});
	},
	initialize: function() {
		UT.dashUtils.assign();
	},
};
P.when('jQuery').execute(function(){
	loadJS('/resources/dashboard/metisMenu/dist/metisMenu.min.js', function() { 
        P.register('metisMenu');
    });
});
P.when('jQuery').execute(function(){
	loadJS('/resources/dashboard/metisMenu/dist/metisMenu.min.js', function() { 
        P.register('MetisMenu');
    });
	loadJS('resources/js/jquery-loader.js', function() { 
        P.register('spin');
    });
	P.when('jQuery').execute(function() {
	 	loadJS('resources/js/bootbox.min.js', function() { 
	         P.register('BootBox');
	     });
	 });
});
P.when('jQuery').execute(function() {
	loadJS('resources/dashboard/js/jquery.dataTables.min.js', function() { 
		P.register('DataTable');
    });
});

P.when('MetisMenu', 'DataTable', 'spin').execute(function(){
	loadJS('resources/dashboard/js/sb-admin-2.js', function() { 
    });
});

P.when('MetisMenu', 'DataTable', 'spin').execute(function() {
	P.register('Dashboard');
	UT.dashUtils.initialize();
});
</script>