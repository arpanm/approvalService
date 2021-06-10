import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IndividualApprovalStatusDetailComponent } from './individual-approval-status-detail.component';

describe('Component Tests', () => {
  describe('IndividualApprovalStatus Management Detail Component', () => {
    let comp: IndividualApprovalStatusDetailComponent;
    let fixture: ComponentFixture<IndividualApprovalStatusDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [IndividualApprovalStatusDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ individualApprovalStatus: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(IndividualApprovalStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IndividualApprovalStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load individualApprovalStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.individualApprovalStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
