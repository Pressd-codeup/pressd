let zipcode = document.forms.zipcode_form.zip.value;
let range = document.forms.zipcode_form.range.value;

function zipcodeFilter(zipcode) {
	console.log(zipcode)

	let settings = {
		"url": "https://www.zipcodeapi.com/rest/" + zipKey + "/radius.csv/" + zipcode + "/" + range + "/miles?minimal",
		"method": "GET"
	}
	$.ajax(settings).done(function (response) {
		const params = document.getElementById("params");
		params.value = response;
	});
}

document.getElementById("set_filter").addEventListener("click", function () {
	zipcodeFilter(zipcode);
});
