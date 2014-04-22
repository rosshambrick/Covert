Covert
===========

Stop worrying about blocking the UI thread and let Covert perform all model opperations in the background, reporting back when the work is done.


## Features
* All Commands and Queries run on a background thread
* Commands and Queries are easy to test since they are just POJOs
* Can run either in parrellel or serially 
* Queries are automatically cached globally
* Easily configure number of threads in threadpool.  [Defaults to one background thread]
* Designed to work well with Otto/EventBus for decoupled pub/sub
* Designed to work well with Dagger/RoboGuice for dependency injection


## Setup (Dagger or RoboGuice)
```
    //inside module
    
    @Provides @Singleton
    DependencyInjector provideInjector() {
        return new DependencyInjector() {
            @Override
            public void inject(Object object) {
                mObjectGraph.inject(object);
            }
        };
    }

    @Provides @Singleton
    Covert provideCovert(DependencyInjector dependencyInjector) {
        return new CovertAgent(dependencyInjector);
    }

```

##Commands
#### Define a Command
```
public class UpdateAddress extends Command {
    @Inject LocalDatabase mLocalDatabase;
    @Inject WebService mWebService;
    
    private long mContactId;
    private String mNewAddress;

	public UpdateAddress(long contactId, String newAddress) {
		mContactId = contactId;
		mNewAddress = newAddress;
	}

	//runs on a background thread
    public void execute() {  
        Contact contact = mLocalDatabase.findById(mContactId);

		if (contact.getAddress().equals(mNewAddress)) {
			//nothing to do
			return;
		}
		
		contact.setAddress(mNewAddress);
		mWebService.update(contact);  //network I/O
		mLocalDatabase.update(contact);  //disk I/O
    }
}
```

### Running a Command
```
public void onSaveClicked() {
	String newAddress = mAddressTextView.getText().ToString();
	mCovert.send(new UpdateAddressCommand(mContactId, newAddress), this);
}
```

### Handling the result
```
public void commandComplete(UpdateAddress command) {
	Toast.makeText(this, "Address updated successfully", Toast.LENGTH_LONG).show();
}

public void commandFailed(CommandError event) {
    String commandName = event.getCommand().getClass().getSimpleName();
    Toast.makeText(getActivity(), commandName + " command failed", Toast.LENGTH_LONG).show();
}
```

##Queries
### Defining a Query
```
public class ContactQuery extends Query<Contact> {
    @Inject LocalDatabase mLocalDatabase;
    @Inject WebService mWebService;
    
    private long mContactId;

	public ContactQuery(long contactId) {
		mContactId = contactId;
	}

	//runs on a background thread
    public Contact load() {  
        Contact contact = mLocalDatabase.findById(mContactId); //disk I/O
		if (contact == null) {			
			contact = mWebService.findById(mContactId); //network I/O
		}		
		return contact;
    }
}
```

### Running a Query
```
public void onResume() {
	mCovert.load(new ContactQuery(mContactId), this); //returns immediately if cached
}
```

### Handling the result
```
public void loadComplete(Contact contact) {
	display(contact);
}

public void loadFailed(ContactQuery query) {
    Toast.makeText(getActivity(), query.getError().getMessage(), Toast.LENGTH_LONG).show();
}
```