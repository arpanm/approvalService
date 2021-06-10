import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApprovalLevelStatusDetailComponent } from './approval-level-status-detail.component';

describe('Component Tests', () => {
  describe('ApprovalLevelStatus Management Detail Component', () => {
    let comp: ApprovalLevelStatusDetailComponent;
    let fixture: ComponentFixture<ApprovalLevelStatusDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ApprovalLevelStatusDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ approvalLevelStatus: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ApprovalLevelStatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApprovalLevelStatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load approvalLevelStatus on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.approvalLevelStatus).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
