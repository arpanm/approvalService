jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { IndividualApprovalStatusService } from '../service/individual-approval-status.service';
import { IIndividualApprovalStatus, IndividualApprovalStatus } from '../individual-approval-status.model';
import { IApprovalRequest } from 'app/entities/approval-request/approval-request.model';
import { ApprovalRequestService } from 'app/entities/approval-request/service/approval-request.service';
import { IApprover } from 'app/entities/approver/approver.model';
import { ApproverService } from 'app/entities/approver/service/approver.service';

import { IndividualApprovalStatusUpdateComponent } from './individual-approval-status-update.component';

describe('Component Tests', () => {
  describe('IndividualApprovalStatus Management Update Component', () => {
    let comp: IndividualApprovalStatusUpdateComponent;
    let fixture: ComponentFixture<IndividualApprovalStatusUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let individualApprovalStatusService: IndividualApprovalStatusService;
    let approvalRequestService: ApprovalRequestService;
    let approverService: ApproverService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [IndividualApprovalStatusUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(IndividualApprovalStatusUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IndividualApprovalStatusUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      individualApprovalStatusService = TestBed.inject(IndividualApprovalStatusService);
      approvalRequestService = TestBed.inject(ApprovalRequestService);
      approverService = TestBed.inject(ApproverService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ApprovalRequest query and add missing value', () => {
        const individualApprovalStatus: IIndividualApprovalStatus = { id: 456 };
        const request: IApprovalRequest = { id: 84513 };
        individualApprovalStatus.request = request;

        const approvalRequestCollection: IApprovalRequest[] = [{ id: 30018 }];
        spyOn(approvalRequestService, 'query').and.returnValue(of(new HttpResponse({ body: approvalRequestCollection })));
        const additionalApprovalRequests = [request];
        const expectedCollection: IApprovalRequest[] = [...additionalApprovalRequests, ...approvalRequestCollection];
        spyOn(approvalRequestService, 'addApprovalRequestToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ individualApprovalStatus });
        comp.ngOnInit();

        expect(approvalRequestService.query).toHaveBeenCalled();
        expect(approvalRequestService.addApprovalRequestToCollectionIfMissing).toHaveBeenCalledWith(
          approvalRequestCollection,
          ...additionalApprovalRequests
        );
        expect(comp.approvalRequestsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Approver query and add missing value', () => {
        const individualApprovalStatus: IIndividualApprovalStatus = { id: 456 };
        const approver: IApprover = { id: 56390 };
        individualApprovalStatus.approver = approver;

        const approverCollection: IApprover[] = [{ id: 98475 }];
        spyOn(approverService, 'query').and.returnValue(of(new HttpResponse({ body: approverCollection })));
        const additionalApprovers = [approver];
        const expectedCollection: IApprover[] = [...additionalApprovers, ...approverCollection];
        spyOn(approverService, 'addApproverToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ individualApprovalStatus });
        comp.ngOnInit();

        expect(approverService.query).toHaveBeenCalled();
        expect(approverService.addApproverToCollectionIfMissing).toHaveBeenCalledWith(approverCollection, ...additionalApprovers);
        expect(comp.approversSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const individualApprovalStatus: IIndividualApprovalStatus = { id: 456 };
        const request: IApprovalRequest = { id: 7963 };
        individualApprovalStatus.request = request;
        const approver: IApprover = { id: 25850 };
        individualApprovalStatus.approver = approver;

        activatedRoute.data = of({ individualApprovalStatus });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(individualApprovalStatus));
        expect(comp.approvalRequestsSharedCollection).toContain(request);
        expect(comp.approversSharedCollection).toContain(approver);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const individualApprovalStatus = { id: 123 };
        spyOn(individualApprovalStatusService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ individualApprovalStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: individualApprovalStatus }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(individualApprovalStatusService.update).toHaveBeenCalledWith(individualApprovalStatus);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const individualApprovalStatus = new IndividualApprovalStatus();
        spyOn(individualApprovalStatusService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ individualApprovalStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: individualApprovalStatus }));
        saveSubject.complete();

        // THEN
        expect(individualApprovalStatusService.create).toHaveBeenCalledWith(individualApprovalStatus);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const individualApprovalStatus = { id: 123 };
        spyOn(individualApprovalStatusService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ individualApprovalStatus });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(individualApprovalStatusService.update).toHaveBeenCalledWith(individualApprovalStatus);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackApprovalRequestById', () => {
        it('Should return tracked ApprovalRequest primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApprovalRequestById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackApproverById', () => {
        it('Should return tracked Approver primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackApproverById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
