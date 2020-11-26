(

ClioLibrary.catalog ([\func, \env], {

	~perc = ClioSynthFunc({ arg kwargs;

		var env, level = kwargs[\synth][\amp] ? 0.9;

		env = EnvGen.kr(Env.perc(
			attackTime: kwargs[\attackTime],
			releaseTime: kwargs[\releaseTime],
			level:level,
			curve: kwargs[\curve],
		), doneAction:kwargs[\doneAction]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] * env;

	}, *[ // sets default kwargs:
		doneAction:2,
		attackTime:0.01,
		releaseTime:2,
		curve:-4,
	]);

	// =====================================================================================

	~asr = ClioSynthFunc({ arg kwargs;

		var sustainLevel = kwargs[\synth][\amp] ? 0.9;

		var env = EnvGen.kr(Env.asr(
			attackTime: kwargs[\attackTime],
			sustainLevel: sustainLevel,
			releaseTime: kwargs[\releaseTime],
			curve: kwargs[\curve],
		), gate:kwargs[\synth][\gate], doneAction:kwargs[\doneAction]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] * env;

	}, *[ // sets default kwargs:
		doneAction:2,
		attackTime:0.01,
		releaseTime:2,
		curve:-4,
	]);

	// =====================================================================================

	~adsr = ClioSynthFunc({ arg kwargs;

		var peakLevel = kwargs[\synth][\amp] ? 0.9;

		var env = EnvGen.kr(Env.adsr(
			attackTime: kwargs[\attackTime],
			decayTime: kwargs[\decayTime],
			sustainLevel: kwargs[\sustainLevel],
			releaseTime: kwargs[\releaseTime],
			peakLevel: peakLevel,
			curve: kwargs[\curve],
		), gate:kwargs[\synth][\gate], doneAction:kwargs[\doneAction]);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] * env;

	}, *[ // sets default kwargs:
		doneAction:2,
		attackTime:0.01,
		decayTime:0.3,
		sustainLevel:0.6,
		releaseTime:2,
		curve:-4,
	]);


	~mesa = ClioSynthFunc({ arg kwargs;

		var env = EnvGen.kr(Env.xyc([
			[0, 0, kwargs[\upCurve]],
			[kwargs[\upTime],
				kwargs[\upLevel], kwargs[\mesaCurve]],
			[kwargs[\upTime]+kwargs[\mesaTime],
				kwargs[\mesaLevel], kwargs[\downCurve]],
			[kwargs[\upTime]+kwargs[\mesaTime]+kwargs[\downTime],
				kwargs[\downLevel], kwargs[\fadeCurve]],
			[kwargs[\upTime]+kwargs[\mesaTime]+kwargs[\downTime]+kwargs[\fadeTime],
				0, 0]
		]), gate:kwargs[\synth][\gate], doneAction:kwargs[\doneAction]);



		kwargs[\synth][\sig] = kwargs[\synth][\sig] * env * kwargs[\synth][\amp];

	}, *[ // sets default kwargs:
		doneAction:2,
		upCurve:24,
		upTime:2,
		upLevel:1,
		mesaCurve:\lin,
		mesaTime:0.5,
		mesaLevel:0.8,
		downCurve:-8,
		downTime:0.2,
		downLevel:0.2,
		fadeCurve:\lin,
		fadeTime:2,
	]);

	// =====================================================================================

	// same a ~perc with different defaults, but worth defining
	// separately since it's very useful
	~swell = ~perc.mimic(*[attackTime:2, releaseTime:0.01, curve:2]);

	// =====================================================================================

}, { arg key, env;
	// f = env.asDict[\perc];
	// env[\perc].();
});


)

