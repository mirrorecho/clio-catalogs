(

ClioLibrary.catalog ([\func], {

	~synthArgs = ClioSynthFunc({ arg kwargs;

		var myKwargs = kwargs.reject {arg v,n; n==\synth;};

		myKwargs.pairsDo { arg name, value;
			kwargs[\synth][name] = value;
		};

	});

	// =====================================================================================

	~silenceFree = ClioSynthFunc({ arg kwargs;

		DetectSilence.ar( kwargs[\synth][\sig], doneAction:kwargs[\doneAction] );

	}, *[ // defaults:
		doneAction:2,
	]);

	// =====================================================================================

	~ampScale = ClioSynthFunc({ arg kwargs;
		kwargs[\synth][\sig] = kwargs[\synth][\sig] * kwargs[\synth][\amp] ? 1;
	});

	// TO DO... add AmpComp, possibly others


}
);

)
